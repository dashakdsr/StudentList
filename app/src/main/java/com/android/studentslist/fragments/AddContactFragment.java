package com.android.studentslist.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.studentslist.R;

import java.util.ArrayList;

public class AddContactFragment extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View form = null;
    final private String LOG_SPAWNER = getClass().getSimpleName();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater()
                .inflate(R.layout.add_contact_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return (builder.setTitle(R.string.add_contact).setView(form)
                .setPositiveButton("OK", this)
                .setNegativeButton("CANCEL", null).create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        String name = ((EditText) form.findViewById(R.id.name_contact)).getText().toString();
        String phoneNumber = ((EditText) form.findViewById(R.id.phone_contact)).getText().toString();

        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();

        operationList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        operationList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name)
                .build());

        operationList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e) {
            Log.e(LOG_SPAWNER, "Failed to contact: ", e);
        }
    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }

    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}