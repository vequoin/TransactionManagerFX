package com.example.rubankfx;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a database to manage multiple accounts.
 * Provides functionalities for opening, closing, depositing into, and withdrawing from accounts.
 * Also supports sorting and printing account data.
 *
 * @author Arun Felix, Digvijay Singh
 */
public class AccountDatabase {

    /** Array to store the accounts. */
    private Account [] accounts;

    /** Number of active accounts in the database. */
    private int numAcct;

    private String lastMessage;

    /**
     * Default constructor initializing the database with default size.
     */
    public AccountDatabase(){
        this.accounts = new Account[4];
        this.numAcct = 0;
    }

    /**
     * Finds the index number of an account.
     * @param account obj
     * @return i index of the account found
     */
    private int find(Account account) {
        for(int i = 0; i < numAcct; i++){
            if(accounts[i].equals(account)){
                return i;
            }
        }
        return -1;
    }

    public Account[] getAllAccounts() {
        Account[] activeAccounts = new Account[numAcct];
        if(numAcct == 0){
            return null;
        }
        for(int i = 0; i < numAcct; i++) {
            activeAccounts[i] = accounts[i];
        }
        return activeAccounts;
    }


    /**
     * Will make a new array of size accounts + 4, copy data from old array,
     * the point the old array to the new array.
     */
    private void grow() {
        Account [] temp = new Account[accounts.length + 4];
        for(int i = 0; i < accounts.length; i++){
            temp[i] = accounts[i];
        }
        accounts = temp;

    }

    /**
     * Checks if database contains given account.
     * @param account obj
     * @return True if account was found, False otherwise.
     */
    public boolean contains(Account account){
        return find(account) != -1;
    }

    /**
     * Given a valid account, the account will be added to database.
     * @param account obj
     * @return true if the account was added successfully, false otherwise.
     */
    public boolean open(Account account){
        if(account == null){
            lastMessage = "AM";
            return false;
        }
        else if(contains(account)){
            lastMessage = "AE";
            return false;
        }
        accounts[numAcct++] = account;
        if(numAcct >= accounts.length){
            grow();
        }
        return true;
    } //add a new account

    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * closes account
     * @param account object
     * @return false if close was unsuccessful, or true if it was successful.
     */
    public boolean close(Account account){
        if(account != null){
            for(int i = 0; i < numAcct; i++){
                if(account.equals(accounts[i])){
                    accounts[i] = null;
                    shiftLeft(i);
                    return true;
                }
            }
        }
        return false;

    } //remove the given account
    /**
     * Shifts contents of the array to the left.
     * @param index an integer representing the index of the array from where the values must be shifted
     */
    private void shiftLeft(int index) {
        if (index >= 0 && index < numAcct - 1) { // Check should be 'index < numAcct - 1' instead of 'index < numAcct + 1'
            for (int i = index; i < numAcct - 1; i++) { // Loop until the second to last element
                accounts[i] = accounts[i + 1];
            }
            accounts[numAcct - 1] = null; // Set the last account to null
        }
        numAcct--; // Decrease the number of accounts after shifting left
    }

    public Account getAccountByProfileAndType(Profile profile,String type) {
        for (int i = 0; i < numAcct; i++) {
            if(accounts[i].getProfile().equals(profile) && accounts[i].GetType().equals(type)){
                return accounts[i];
            }
        }
        return null;
    }



    /**
     * This will assign the Account index to the last non null account in array to prevent null pointer exception
     * @return new array index containing last account.
     */
    private int newAcct(){
        int i;
        for(i = 0; i < accounts.length && accounts[i] != null; i++){
            i++;
        }
        return i - 1;
    }
    /**
     * Withdraws money from accounts.
     * @param account obj
     * @return true if withdrawn successfully, false otherwise.
     */
    public boolean withdraw(Account account){
        if(account != null){
            for(int i = 0; i < numAcct; i++){
                if(account.equals(accounts[i])){
                    accounts[i].withdraw(account.getbalance());
                    return true;
                }
            }
        }
        return false;
    } //false if insufficient fund

    /**
     * Deposits the specified amount into the given account.
     * @param account the account to which the deposit is made.
     */
    public void deposit(Account account){
        if(account == null){
            return;
        }
        for(int i = 0; i < numAcct; i++){
            if(account.equals(accounts[i])){
                accounts[i].deposit(account.getbalance());
            }
        }
    }


    /**
     * Prints the list of accounts sorted by account type and profile.
     */
    public void Sort(){
        if(numAcct < 1){
            return;
        }
        quicksort(0, numAcct - 1);
    } //sort by account type and profile



    /**
     * Prints the fees and interest rates with the accounts
     */
    public List<String> getInterestInfo(){

        List<String> InfoList = new ArrayList<>();
        if(numAcct < 1){
            return null;
        }
        for(int i = 0; i < numAcct; i++){
            String interestInfo = accounts[i].toString() + "::fee $" + accounts[i].monthlyFee() + "::monthly interest $" + accounts[i].monthlyInterest();
            InfoList.add(interestInfo);
        }
        InfoList.add("*end of list.");
        return InfoList;
    }

    /**
     * Updates account balances by applying interests and fees.
     */
    public void updateBalances(){
        if(numAcct < 1){
            return;
        }
        for(int i = 0; i < numAcct; i++){
            accounts[i].withdraw(accounts[i].monthlyFee()+accounts[i].monthlyInterest());
        }
    }

    /**
     * Sorts the accounts based on the account type and profile.
     * Utilizes the quicksort algorithm.
     *
     * @param lo the lower index bound for the sort.
     * @param hi the upper index bound for the sort.
     */
    private void quicksort(int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int pivotIndex = partition(lo, hi);
        quicksort(lo, pivotIndex - 1);
        quicksort(pivotIndex + 1, hi);
    }

    /**
     * Determines the pivot's correct position in the sorted version of the array.
     * Used in the quicksort algorithm.
     *
     * @param lo the lower index bound for the partition.
     * @param hi the upper index bound for the partition.
     * @return the pivot's correct position index.
     */
    private int partition(int lo, int hi) {
        Account pivot = accounts[lo];
        int L = lo + 1;
        int R = hi;
        while (true) {
            while (L <= R && accounts[L].compareTo(pivot) <= 0) {
                L++;
            }
            while (R >= L && accounts[R].compareTo(pivot) > 0) {
                R--;
            }
            if (L < R) {
                swap(L, R);
            } else {
                break;
            }
        }
        swap(lo, R);
        return R;
    }

    /**
     * Swaps two accounts in the database based on their indices.
     *
     * @param i the index of the first account.
     * @param j the index of the second account.
     */
    private void swap(int i, int j) {
        Account temp = accounts[i];
        accounts[i] = accounts[j];
        accounts[j] = temp;
    }

}
