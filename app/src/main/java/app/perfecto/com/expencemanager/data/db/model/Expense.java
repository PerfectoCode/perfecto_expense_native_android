package app.perfecto.com.expencemanager.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "expenses")
public class Expense implements Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("expenseHead")
    @Expose
    private String expenseHead;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("expenseCategory")
    @Expose
    private String expenseCategory;
    @SerializedName("isRecurring")
    @Expose
    private Boolean isRecurring;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("attachements")
    @Expose
    private String attachemnts;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Ignore
    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenseHead() {
        return expenseHead;
    }

    public void setExpenseHead(String expenseHead) {
        this.expenseHead = expenseHead;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public Boolean getRecurring() {
        return isRecurring;
    }

    public void setRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAttachemnts() {
        return attachemnts;
    }

    public void setAttachemnts(String attachemnts) {
        this.attachemnts = attachemnts;
    }

    public Expense() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.date);
        dest.writeString(this.expenseHead);
        dest.writeValue(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.expenseCategory);
        dest.writeValue(this.isRecurring);
        dest.writeString(this.details);
        dest.writeString(this.attachemnts);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected Expense(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.date = in.readString();
        this.expenseHead = in.readString();
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.currency = in.readString();
        this.expenseCategory = in.readString();
        this.isRecurring = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.details = in.readString();
        this.attachemnts = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel source) {
            return new Expense(source);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}
