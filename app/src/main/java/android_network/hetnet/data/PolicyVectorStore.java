package android_network.hetnet.data;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;


public class PolicyVectorStore {

    private final int MAX_DATABASE_SIZE = 5000;

    // Database fields
    private SQLiteDatabase database;
    private PolicyVectorStoreHelper dbHelper;
    private List<PolicyRuleVector> storedVectors;

    public PolicyVectorStore(Context context) {
        dbHelper = new PolicyVectorStoreHelper(context);
    }


    public void addPolicyVector(PolicyRuleVector v) {
        dbHelper.storePolicyVector(v);
    }


    public void sync() {
        //TODO: Sync with web db
    }


}
