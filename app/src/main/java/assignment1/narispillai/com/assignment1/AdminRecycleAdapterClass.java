package assignment1.narispillai.com.assignment1;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.widget.Space;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.AppCompatTextView;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.util.List;

        import static assignment1.narispillai.com.assignment1.R.id.parent;
        import static assignment1.narispillai.com.assignment1.R.id.visible;
        import static java.security.AccessController.getContext;

public class AdminRecycleAdapterClass extends RecyclerView.Adapter<AdminRecycleAdapterClass.ViewHolder>{

    private List<AdminUserClass> adminlist;
    private AdminDatabaseClass addatabase;
    private AdminUserClass adminuser;
    boolean checkdelete=false;

    AdminList admnlist = new AdminList();

    //Constructor for the Adapter (RecycleView Adapter)
    public AdminRecycleAdapterClass(List<AdminUserClass> adminlist) {
        this.adminlist = adminlist;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_itemview, parent, false);
        return new ViewHolder(itemView);

    }


    //Creating view for every object/admin in the arraylist
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final View view = null;

        holder.adminname.setText(adminlist.get(position).getUsername());
        holder.adminbirthday.setText(adminlist.get(position).getBirthday());
        holder.admingender.setText(adminlist.get(position).getGender());
        holder.adminemail.setText(adminlist.get(position).getEmail());
        holder.adminposition.setText(adminlist.get(position).getPosition());
        holder.adminaddress.setText(adminlist.get(position).getAddress());
        holder.adminphone.setText(adminlist.get(position).getPhoneNumber());

        //Used to check if any of the card view/holder is clicked and set the second layout visible for the clicked card view and invisible for any previous card view.
        holder.llfl.setVisibility(adminlist.get(position).ischeck()?View.VISIBLE: View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Declaration of floating button for all the button
                FloatingActionButton call = (FloatingActionButton) view.findViewById(R.id.CallIcon);
                FloatingActionButton message = (FloatingActionButton) view.findViewById(R.id.MessageIcon);
                FloatingActionButton email = (FloatingActionButton) view.findViewById(R.id.EmailIcon);
                FloatingActionButton navigation = (FloatingActionButton) view.findViewById(R.id.NavigationIcon);
                FloatingActionButton share = (FloatingActionButton) view.findViewById(R.id.shareContact);

                final FloatingActionButton delete = (FloatingActionButton) view.findViewById(R.id.deleteIcon);
                final FloatingActionButton edit = (FloatingActionButton) view.findViewById(R.id.editIcon);
                final FloatingActionButton supportbutton1 = (FloatingActionButton) view.findViewById(R.id.supportbutton1);
                final FloatingActionButton supportbutton2 = (FloatingActionButton) view.findViewById(R.id.supportbutton2);
                //Space spacer1 = (Space) view.findViewById(R.id.space1);
                //Space spacer2 = (Space) view.findViewById(R.id.space2);

                addatabase = new AdminDatabaseClass(view.getContext());

                //Setting the boolean variable for the second view.
                for (AdminUserClass i : adminlist){
                    i.setIscheck(false);
                }
                adminlist.get(position).setIscheck(true);

                //Changing the view of the view when user click on different card view.
                notifyDataSetChanged();

                //String declaration for CEO checking.
                final String ceposi = AdminList.nama;

                //Return String of the admin position.
                String realdeal = addatabase.checkCEOName(ceposi);

                //Checks if the logged admin is the CEO of the company/organization.
                if(realdeal.equals("Chief Executive Officer(CEO)")){
                    //Show the delete and edit option for the admin.
                    delete.show();
                    edit.show();
                    supportbutton1.setVisibility(View.INVISIBLE);
                    supportbutton2.setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.space1).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.space2).setVisibility(View.VISIBLE);
                }
                else{
                    //Hide the delete and edit option.
                    delete.hide();
                    edit.hide();
                    supportbutton1.setVisibility(View.GONE);
                    supportbutton2.setVisibility(View.GONE);
                    view.findViewById(R.id.space1).setVisibility(View.GONE);
                    view.findViewById(R.id.space2).setVisibility(View.GONE);
                }


                //String value of the selected admin name.
                final String admin = holder.adminname.getText().toString();

                final UserNavigation un = new UserNavigation();

                //Call function declaration
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //String value of the selected admin phone number
                        String ph = holder.adminphone.getText().toString();

                        //Calling the calling method from the User Navigation activity.
                        un.callcaller(view.getContext(), ph, admin);

                    }
                });


                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //String value of the selected admin phone number
                        String ph = holder.adminphone.getText().toString();

                        //Calling the message method from the User Navigation activity.
                        un.messagemessengger(view.getContext(), ph, admin);

                    }
                });


                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //String value of the selected admin email address
                        String pemail = holder.adminemail.getText().toString();

                        //Intent declaration for the email.
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        //Important elements in order to send email. "message/rfc822" will only display messaging and email application in the app chooser.
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pemail});
                        emailIntent.putExtra(Intent.EXTRA_CC, pemail);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Team Target");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent from Team Target");

                        try {
                            //Display available email application in the user mobile.
                            view.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            //Error message if there's no email application.
                            Toast.makeText(view.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                //Navigation function declaration
                navigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Snackbar.make(view, "Please wait...", Snackbar.LENGTH_LONG)
                                .setAction("Close", null)
                                .show();

                        //String value of the selected admin physical address
                        String address = holder.adminaddress.getText().toString();

                        //Launch map activity together with the adminlocation marked on the activity.
                        Intent i = new Intent(view.getContext(), LocationActivity.class);
                        i.putExtra("ADDRESS", address);
                        i.putExtra("Name", admin);
                        view.getContext().startActivity(i);

                    }
                });

                //Admin information sharing button function
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Getting the admin information and converting them into string.
                        String nm = holder.adminname.getText().toString();
                        String dob = holder.adminbirthday.getText().toString();
                        String gen = holder.admingender.getText().toString();
                        String mail = holder.adminemail.getText().toString();
                        String pos = holder.adminposition.getText().toString();
                        String add = holder.adminaddress.getText().toString();
                        String pho = holder.adminphone.getText().toString();

                        //Composing all the string into one
                        String admininfo =  "Name : " + nm + "\n" +
                                "Birthday : " + dob + "\n" +
                                "Gender : " + gen + "\n" +
                                "Email : " + mail + "\n" +
                                "Position : " + pos + "\n" +
                                "Address : " + add + "\n" +
                                "Phone Number : " + pho + "\n";

                        //Intent declaration for info sharing
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        //Display every application that allows text sharing.
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, admininfo);

                        try {
                            view.getContext().startActivity(Intent.createChooser(emailIntent, "Send info..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(view.getContext(), "There is no social media sharing applications installed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                //Delete function declaration
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        //Admin name in string datatype
                        final String nm = holder.adminname.getText().toString();

                        //If the admin is the CEO, he/she is reqired to use the remove me option in the navigation menu.
                        if(nm.equals(ceposi)){
                            Snackbar.make(view, "Please use 'Remove Me' option in the navigation menu.", Snackbar.LENGTH_LONG)
                                    .setAction("Close", null)
                                    .show();
                        }
                        else{
                            //Asks if the CEO really wants to delete the admin from the database.
                            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                            alert.setIcon(R.drawable.adminremove);
                            alert.setMessage("Do you really want to remove " + nm + " from the database?");
                            alert.setTitle(" ");
                            alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //I yes, the app delete the admin and refresh the list.
                                    addatabase = new AdminDatabaseClass(view.getContext());
                                    adminuser = new AdminUserClass();
                                    adminuser.setUsername(nm);
                                    addatabase.deleteAdmin(adminuser);

                                    adminlist.remove(position);
                                    notifyDataSetChanged();

                                    Toast.makeText(view.getContext(), nm + " admin user deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alert.setPositiveButton("No", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    //Does nothing
                                    Toast.makeText(view.getContext(), "No changes were done on " + nm, Toast.LENGTH_LONG).show();
                                }
                            });
                            alert.show();
                        }
                    }
                });

                //Edir function declaration
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Launch edit activity and pass the admin name for data retrieval.
                        Intent i = new Intent(view.getContext(), EditActivity.class);
                        i.putExtra("Name", admin);
                        view.getContext().startActivity(i);

                    }
                });

            }


        });
    }



    public View getView(int position, ViewHolder hol,ViewGroup parent){

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_itemview, parent, false);
        return itemView;

    }

    @Override
    public int getItemCount() {
        Log.v(AdminRecycleAdapterClass.class.getSimpleName(),""+adminlist.size());
        return adminlist.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        //View holder declaration for all the elements in the recycle view(card view)

        public AppCompatTextView adminname;
        public AppCompatTextView adminbirthday;
        public AppCompatTextView admingender;
        public AppCompatTextView adminemail;
        public AppCompatTextView adminposition;
        public AppCompatTextView adminaddress;
        public AppCompatTextView adminphone;
        private LinearLayout llfl;

        public ViewHolder(final View itemView) {
            super(itemView);

            //Wiring the database element to text view in the card view.
            adminname = (AppCompatTextView) itemView.findViewById(R.id.textViewUsername);
            adminbirthday = (AppCompatTextView) itemView.findViewById(R.id.textViewBirthday);
            admingender = (AppCompatTextView) itemView.findViewById(R.id.textViewGender);
            adminemail = (AppCompatTextView) itemView.findViewById(R.id.textViewEmail);
            adminposition = (AppCompatTextView) itemView.findViewById(R.id.textViewPosition);
            adminaddress = (AppCompatTextView) itemView.findViewById(R.id.textViewAddress);
            adminphone = (AppCompatTextView) itemView.findViewById(R.id.textViewPhone);
            llfl= (LinearLayout) itemView.findViewById(R.id.floatingview);
        }
    }

}
