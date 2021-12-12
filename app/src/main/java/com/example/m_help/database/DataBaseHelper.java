package com.example.m_help.database;

import com.example.m_help.dataClasses.BloodBank;
import com.example.m_help.dataClasses.BloodGroup;
import com.example.m_help.dataClasses.Donor;
import com.example.m_help.dataClasses.Hospital;
import com.example.m_help.dataClasses.Patient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.List;

import kotlin.Pair;

public class DataBaseHelper {

    public static final String HOSPITALS = "hospitals";
    public static final String DONORS = "donors";
    public static final String PATIENTS = "patients";
    public static final String BLOOD_BANKS = "blood banks";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void registerDonor(String email, Donor donor , Listener<Void> listener){
        addData(email, DONORS, donor, listener);
    }

    public void registerPatient(String email, Patient patient , Listener<Void> listener){
        addData(email, PATIENTS, patient, listener);
    }

    public void registerHospital(String email, Hospital hospital , Listener<Void> listener){
        addData(email, HOSPITALS, hospital, listener);
    }

    public void registerBloodBank(String email, BloodBank bloodBank , Listener<Void> listener){
        addData(email, BLOOD_BANKS, bloodBank, listener);
    }

    public void getPatient(String email, Listener<Patient> listener){
        getData(email, PATIENTS, listener, Patient.class);
    }

    public void getHospital(String email, Listener<Hospital> listener){
        getData(email, HOSPITALS, listener, Hospital.class);
    }

    public void getDonor(String email, Listener<Donor> listener){
        getData(email, DONORS, listener, Donor.class);
    }

    public void getBloodBank(String email, Listener<BloodBank> listener){
        getData(email, BLOOD_BANKS, listener, BloodBank.class);
    }

    private <T> void addData(String email, String collectionName, T t, Listener<Void> listener){
        db.collection(collectionName)
                .document(email)
                .set(t)
                .addOnSuccessListener(unused -> {
                    listener.onSuccess(null);
                })
        .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    private <T> void getData(String email, String collectionName, Listener<T> listener, Class<T> type){
        db.collection(collectionName)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        T t = documentSnapshot.toObject(type);
                        listener.onSuccess(t);
                    }else{
                        listener.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public void updateHospitalBloodData(String email,BloodGroup bloodGroup, Listener<BloodGroup> listener){
        updateData(email, HOSPITALS, "bloodGroup", bloodGroup, listener);
    }

    public void updateBloodBankBloodData(String email,BloodGroup bloodGroup, Listener<BloodGroup> listener){
        updateData(email, BLOOD_BANKS, "bloodGroup", bloodGroup, listener);
    }

    public void updateOxygenCylinder(String email, int data, Listener<Integer> listener){
        updateData(email, HOSPITALS, "availableOxygenCylinder", data, listener);
    }

    public void updateOxygen(String email, double data, Listener<Double> listener){
        updateData(email, HOSPITALS, "availableOxygen", data, listener);
    }

    public void updateIcuBeds(String email, int data, Listener<Integer> listener){
        updateData(email, HOSPITALS, "availableIcuBed", data, listener);
    }

    public void updateReceiveBloodReq(String email, boolean data, Listener<Boolean> listener){
        updateData(email, DONORS, "receiveForBlood", data, listener);
    }

    public void updateReceivePlasmaReq(String email, boolean data, Listener<Boolean> listener){
        updateData(email, DONORS, "receiveForPlasma", data, listener);
    }

    private <T> void updateData(String email, String collectionName, String field, T t, Listener<T> listener){
        db.collection(collectionName)
                .document(email)
                .update(field, t)
                .addOnSuccessListener(unused -> {
                    listener.onSuccess(t);
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    @SafeVarargs
    public final void fetchSuitableHospitals(String city , Listener<List<Hospital>> listener , Pair<String,Object>... data){
        CollectionReference c = db.collection(HOSPITALS);
        Query q = c.whereEqualTo("address.city", city);
        for ( Pair<String,Object> datum : data ) {
            q = q.whereGreaterThan(datum.getFirst() , datum.getSecond());
        }

        q.get().addOnSuccessListener(queryDocumentSnapshots -> {
            listener.onSuccess(queryDocumentSnapshots.toObjects(Hospital.class));
        }).addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public interface Listener<T>{
        void onSuccess(T t);
        void onFailure(String message);
    }
}

