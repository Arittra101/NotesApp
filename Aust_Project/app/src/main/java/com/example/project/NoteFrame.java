package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class  NoteFrame extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton createNotesfab;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();

    RecyclerView mrecylerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    Query query;
    int sort_check=1;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel,Noteviewholder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frame);

//        dioluge_pop("SuccesFully Log In");

        createNotesfab = findViewById(R.id.createnote);


        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //re_view(sort_check);
        String one = "1";

            query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("bookmark");
            Toast.makeText(getApplicationContext(), "Sorted",Toast.LENGTH_SHORT).show();



        //all user notes collected in allusernotes->>>>>>>
        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
        //user note collect


        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, Noteviewholder>(allusernotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull Noteviewholder noteviewholder, int i, @NonNull firebasemodel firebasemodel) {
                //just set anything use this method

                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                int colorcode = getRandomColor();

                noteviewholder.notetitle.setText(firebasemodel.getTitle());    //model get
                noteviewholder.notecontent.setText(firebasemodel.getContent());

                //check is it zero or One->>>>>>>>>>>>

//                colorCode.add(R.color.red);
//                colorCode.add(R.color.green3);
//                colorCode.add(R.color.yellow);
                String check = "0";
                if(firebasemodel.getBookmark().equals(check))   //first p
                {
                    noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
                    noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
                    noteviewholder.mnote.setBackgroundColor(noteviewholder.itemView.getResources().getColor(R.color.redasole,null));
//                    Toast.makeText(getApplicationContext(),firebasemodel.getBookmark()+ "Already Saved",Toast.LENGTH_SHORT).show();
                }
               else if(firebasemodel.getBookmark().equals("1"))   //first p
                {
                    noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
                    noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
                    noteviewholder.mnote.setBackgroundColor(noteviewholder.itemView.getResources().getColor(R.color.redasole2,null));
//                    Toast.makeText(getApplicationContext(),firebasemodel.getBookmark()+ "Already Saved",Toast.LENGTH_SHORT).show();
                }
                else if(firebasemodel.getBookmark().equals("2"))         //third p
                {
                    noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
                    noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
                    noteviewholder.mnote.setBackgroundColor(noteviewholder.itemView.getResources().getColor(colorcode,null));
//                    Toast.makeText(getApplicationContext(),firebasemodel.getBookmark()+ "Already Saved",Toast.LENGTH_SHORT).show();
                }
                else  if(firebasemodel.getBookmark().equals("3"))       //no pri
                {
                    noteviewholder.bookmarkimageview.setVisibility(View.VISIBLE);
                    noteviewholder.bookmarkdoneview.setVisibility(View.INVISIBLE);
                    noteviewholder.mnote.setBackgroundColor(noteviewholder.itemView.getResources().getColor(colorcode,null));
                }
                //check is it zero or One





                //bookmark deleted bookmardone view ->>>>>>>>>>>>>>
                noteviewholder.bookmarkdoneview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> m = new HashMap<>();

                        String check = "3";
                        m.put("bookmark",check);
                        m.put("title",firebasemodel.getTitle());
                        m.put("content",firebasemodel.getContent());
                        m.put("date",firebasemodel.getDate());
                        m.put("time",firebasemodel.getTime());

                        //update bookmark data zero -> one so that view will be chagnge
                        DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                        documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),firebasemodel.getBookmark()+ "zero to one done",Toast.LENGTH_SHORT).show();
                            }
                        });



                        //delete data also from book mark ->>>>>
                        documentReference =  firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Bookmark").document(docId);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Deleted From BookMark",Toast.LENGTH_SHORT).show();
                            }
                        });
                        //delete data also from book mark

                    }


                });
                //bookmark deleted bookmardone view





                //bookmarkadd set action listener-->>>>>>>>>>
                noteviewholder.bookmarkimageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       // Toast.makeText(getApplicationContext(),firebasemodel.getBookmark()+ "Ok saved",Toast.LENGTH_SHORT).show();
                        String check="3";
//                        if(firebasemodel.getBookmark().equals(check))
//                        {
//                            Toast.makeText(getApplicationContext(),firebasemodel.getBookmark()+ "Already Saved",Toast.LENGTH_SHORT).show();
//                        }
                       // else{
                            FirebaseFirestore root = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = root.collection("notes").document(firebaseUser.getUid()).collection("Bookmark").document(docId);

                            Map<String,Object> m = new HashMap<>();
                            m.put("title",firebasemodel.getTitle());
                            m.put("content",firebasemodel.getContent());

                            documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //here
//                                    noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
//                                    noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(),"OK Done",Toast.LENGTH_SHORT).show();
                                }
                            });

                            documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                            Map<String,Object> m1 = new HashMap<>();
                            String  bk = "2";
                            m1.put("title",firebasemodel.getTitle());
                            m1.put("content",firebasemodel.getContent());
                            m1.put("bookmark",bk);
                            m1.put("date",firebasemodel.getDate());
                            m1.put("time",firebasemodel.getTime());
                            documentReference.set(m1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        //}


                    }
                });
                //bookmarkadd set action listener







                //itemView details intent work here->>>>>>>>>>>>>>>>>
                noteviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(),notedetails.class);
                        intent.putExtra("title",firebasemodel.getTitle());
                        intent.putExtra("content",firebasemodel.getContent());
                        intent.putExtra("noteId",docId);
                        intent.putExtra("Bookmark",firebasemodel.getBookmark());
                        intent.putExtra("date",firebasemodel.getDate());
                        intent.putExtra("time",firebasemodel.getTime());

                        v.getContext().startActivity(intent);
//                        Toast.makeText(getApplicationContext(),"This is clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                //itemView details intent work here







                //popUp button all work here ->>>>>>>>>>>>>>>>>>
                noteviewholder.popupbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent = new Intent(v.getContext(),editnoteactivity.class);
                                intent.putExtra("title",firebasemodel.getTitle());
                                intent.putExtra("content",firebasemodel.getTitle());
                                intent.putExtra("noteId",docId);
                                intent.putExtra("Bookmark",firebasemodel.getBookmark());
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                               documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {

                                       Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(getApplicationContext(),"Failure to Delete",Toast.LENGTH_SHORT).show();
                                   }
                               });

                                Toast.makeText(v.getContext(),"Delete This Note Successfully",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("First priority").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                               DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                               Map<String,Object> m = new HashMap<>();
                               String pr = "0";
                               m.put("content",firebasemodel.getTitle());
                               m.put("title",firebasemodel.getTitle());
                               m.put("bookmark",pr);
                                m.put("date",firebasemodel.getDate());
                                m.put("time",firebasemodel.getTime());
                               documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
//                                       noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
//                                       noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
                                       add_bookmark(noteviewholder,firebasemodel,docId);
                                   }
                               });

                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Second priority").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                Map<String,Object> m = new HashMap<>();
                                String pr = "1";
                                m.put("content",firebasemodel.getTitle());
                                m.put("title",firebasemodel.getTitle());
                                m.put("bookmark",pr);
                                m.put("date",firebasemodel.getDate());
                                m.put("time",firebasemodel.getTime());
                                documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                                       noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
//                                       noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
                                        add_bookmark(noteviewholder,firebasemodel,docId);
                                    }
                                });
                                return false;
                            }
                        });

                        popupMenu.show();
                    }

                });
                //popUp button all work here ->>>>>>>>>>>

            }
            //end of all bindViewholder all the access are here->>>>>>>>>>>>


            @NonNull
            @Override
            public Noteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
              return new Noteviewholder(view);
            }
        };
//
//

        mrecylerview = findViewById(R.id.recycler);
        mrecylerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecylerview.setLayoutManager(staggeredGridLayoutManager);

        mrecylerview.setAdapter(noteAdapter);
        createNotesfab.setOnClickListener(this);     //note add button


    }

    public class Noteviewholder extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        private ImageView popupbutton;
        private ImageView bookmarkimageview;
        private ImageView bookmarkdoneview;
        LinearLayout mnote;
        public Noteviewholder(@NonNull View itemView)
        {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent =  itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
            popupbutton= itemView.findViewById(R.id.menupopbutton);
            bookmarkimageview = itemView.findViewById(R.id.book_mark_icon);
            bookmarkdoneview =  itemView.findViewById(R.id.book_mark_done_icon);

        }
    }


    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.createnote)
        {
            startActivity(new Intent(NoteFrame.this,createNote.class));
            Toast.makeText(getApplicationContext(),"Nice",Toast.LENGTH_SHORT).show();
        }
//        if(v.getId()==R.id.logout)
//        {
//            logOut();
//        }

    }


    public void dioluge_pop(String about)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(NoteFrame.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View v = new View(NoteFrame.this) ;
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.notice_pop,viewGroup,false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button ok = dialogView.findViewById(R.id.ok);
        TextView msg = dialogView.findViewById(R.id.about);
        msg.setText(about);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.LO)
        {
          logOut();
        }
        if(item.getItemId()==R.id.bookmark)
        {
           // finish();
            startActivity(new Intent(getApplicationContext(),Bookmark.class));
        }
        if(item.getItemId() == R.id.sort)
        {

        }
        return super.onOptionsItemSelected(item);
    }



    public void logOut()
    {

        firebaseAuth.signOut();
        finish();
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null)
        {
            noteAdapter.stopListening();
        }
    }

   private int getRandomColor(){
        ArrayList<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.gray);
        colorCode.add(R.color.sky_blue);
        colorCode.add(R.color.sky_blue2);
        colorCode.add(R.color.green);

        colorCode.add(R.color.white1);
        colorCode.add(R.color.white2);
        colorCode.add(R.color.white3);
        colorCode.add(R.color.green2);

        colorCode.add(R.color.Pink);


        Random random = new Random();   //for random input
        int number = random.nextInt(colorCode.size());    //nextInt use for unput ->> Scanner sc = new Scanner(System.in)->> sc.nextInt()-<  one int get Input
        return colorCode.get(number);
   }



  public void add_bookmark(Noteviewholder noteviewholder,firebasemodel firebasemodel,String docId)
  {
      FirebaseFirestore root = FirebaseFirestore.getInstance();
      DocumentReference documentReference = root.collection("notes").document(firebaseUser.getUid()).collection("Bookmark").document(docId);

      Map<String,Object> m = new HashMap<>();
      m.put("title",firebasemodel.getTitle());
      m.put("content",firebasemodel.getContent());

      documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
              //here
//              noteviewholder.bookmarkimageview.setVisibility(View.INVISIBLE);
//              noteviewholder.bookmarkdoneview.setVisibility(View.VISIBLE);
              Toast.makeText(getApplicationContext(),"OK Done",Toast.LENGTH_SHORT).show();
          }
      });

  }


}