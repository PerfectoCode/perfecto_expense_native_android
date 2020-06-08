package app.perfecto.com.expencemanager.ui.expenseDetail;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.ui.base.BaseActivity;
import app.perfecto.com.expencemanager.ui.custom.MultiSelectionSpinner;
import app.perfecto.com.expencemanager.ui.custom.MultiSelectionSpinner.MultiSelectListener;
import app.perfecto.com.expencemanager.utils.AppConstants;
import app.perfecto.com.expencemanager.utils.AppLogger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.gauriinfotech.commons.Commons;


public class ExpenseDetailActivity extends BaseActivity<ExpenseDetailViewModel> {

    public static final String KEY_PARCELABLE_EXPENSE = "KEY_PARCELABLE_EXPENSE";
    public static final String KEY_PARCELABLE_EDIT_MODE = "KEY_PARCELABLE_EDIT_MODE";
    private static final int READ_STORAGE = 121;

    private Expense expense;
    private ExpenseDetailViewModel expenseDetailViewModel;

    private List<String> attachments = new ArrayList<String>();

    BottomSheetDialog mBottomSheetDialog;


    @Inject
    ViewModelProvider.Factory factory;

    @BindView(R.id.tie_expense_head)
    TextInputEditText tieExpenseHead;

    @BindView(R.id.add_date)
    TextInputEditText tieExpenseDate;

    @BindView(R.id.add_amount)
    TextInputEditText tieExpenseAmount;

    @BindView(R.id.add_details)
    TextInputEditText tieExpenseDetails;

    @BindView(R.id.add_category)
    TextInputEditText tieCategory;

    @BindView(R.id.signup_currency)
    Spinner spinnerCurrency;

    @BindView(R.id.add_recurring_toggle)
    Switch switchRecurring;

    @BindView(R.id.mySpinner1)
    MultiSelectionSpinner spinner;

    @BindView(R.id.layout_attachments)
    ConstraintLayout attachemntsLayout;

    @BindView(R.id.amount_seek_bar)
    SeekBar amountSeekBar;

    @BindView(R.id.view_view_attach_btn)
    Button tvViewAttachment;

    @BindView(R.id.layout_buttons)
    LinearLayout layoutBtmButtons;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_save_btn)
    Button btnSave;

    @BindView(R.id.amt_container)
    ConstraintLayout seekBarConstraintLayout;

    @BindView(R.id.removeAttachment)
    Button txtRemoveAttachment;


    @BindView(R.id.add_attach_btn)
    ImageButton btnCapturePicture;


    @BindView(R.id.tv_attachments)
    TextView tvAttachments;

    @BindView(R.id.add_head)
    AppCompatSpinner spinnerHead;

    @BindView(R.id.spinner_category)
    AppCompatSpinner spinnerCategory;



    private Menu menu;


    @Inject
    AttachemntsAdapter attachemntsAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private String pictureFilePath;
    private boolean isEditable = false;
    private DatePickerDialog datePickerDialog;
    private ArrayAdapter<String> currencyAdapter;
    private ArrayAdapter<String> headAdapter;
    private ArrayAdapter<String> categoryAdapter;
    private MaterialButton btnAttachCamera;
    private MaterialButton btnAttachDoc;
    private MaterialButton btnAttachCancel;
    private SimpleDateFormat sdf ;


    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener dateSetListener;

    DialogProperties properties = new DialogProperties();
    FilePickerDialog dialog;



    public static Intent getStartIntent(Context context) {
        return new Intent(context, ExpenseDetailActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expense_details);
        expense = getIntent().getParcelableExtra(KEY_PARCELABLE_EXPENSE);

        if (expense != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                isEditable = bundle.getBoolean(KEY_PARCELABLE_EDIT_MODE, false);
            }
        }

        setUnBinder(ButterKnife.bind(this));

        observeAllEvents();

        setUp();
    }

    @Override
    public ExpenseDetailViewModel getViewModel() {
        expenseDetailViewModel = ViewModelProviders.of(this, factory).get(ExpenseDetailViewModel.class);
        return expenseDetailViewModel;
    }

    private void observeAllEvents() {
        expenseDetailViewModel.getReturnToMainActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        returnToMainActivity();
                    }
                });

        expenseDetailViewModel.getOpenInBrowserEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openInBrowser();
                    }
                });

        expenseDetailViewModel.getNewExpensesAdded().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                expense = null;
                resetFields();
                setUp();
                returnToMainActivity();

            }
        });
        expenseDetailViewModel.getExpenseUpdated().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer aLong) {
                showToast(getString(R.string.expense_updated));
                expense = null;
                resetFields();
                setUp();
                returnToMainActivity();
            }
        });

        expenseDetailViewModel.getPreffferedCurrencyChanged().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spinnerCurrency.setSelection(currencyAdapter.getPosition(s));
            }
        });
    }

    @Override
    protected void setUp() {

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sdf = new SimpleDateFormat(AppConstants.DATE_FORMAT);
        String[] currencyList = getResources().getStringArray(R.array.currency);
        String[] headList = getResources().getStringArray(R.array.head);
        String[] categoryList = getResources().getStringArray(R.array.category);
        currencyAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_head, currencyList);
        headAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_head, headList);
        categoryAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_head, categoryList);
        if(expense == null)
            expenseDetailViewModel.getDefaultCurrency();

        spinner.setItems(getResources().getStringArray(R.array.category));

        spinnerCurrency.setAdapter(currencyAdapter);
        spinner.setListener(new MultiSelectListener() {
            @Override
            public void onItemSelected() {

                tieCategory.setText(spinner.getSelectedItemsAsString());
            }
        });
        spinnerHead.setAdapter(headAdapter);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerHead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*view.requestFocus();
                tieExpenseAmount.clearFocus();*/
                tieExpenseHead.setText(headAdapter.getItem(position));
                //spinnerHead.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //spinnerHead.setVisibility(View.INVISIBLE);
            }
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* view.requestFocus();
                tieExpenseAmount.clearFocus();*/
                if(position==0){
                    tieCategory.setText(" ");
                }else {
                    tieCategory.setText(categoryAdapter.getItem(position));
                        //                    Toast.makeText(getApplicationContext(),categoryAdapter.getItem(position),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tieExpenseDate.setText(sdf.format(myCalendar.getTime()));
                //tieExpenseAmount.clearFocus();
            }

        };
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(Environment.getExternalStorageDirectory().getPath());
        properties.error_dir = new File(Environment.getExternalStorageDirectory().getPath());
        properties.offset = new File(Environment.getExternalStorageDirectory().getPath());
        properties.extensions = null;
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    tieExpenseAmount.setText(String.format("%.2f", progress * 100.00));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Select a File");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                attachments.addAll(Arrays.asList(files));
                attachemntsAdapter.updateListItems(attachments);
                mBottomSheetDialog.dismiss();
                tvViewAttachment.setVisibility(View.VISIBLE);
            }
        });

        tieExpenseAmount.setText(getString(R.string.default_amount));
        addTextChangeListner();

        if (expense != null) {
            tieExpenseAmount.setText(String.format("%.2f", expense.getAmount()));
            tieExpenseHead.setText(expense.getExpenseHead());
            spinnerHead.setSelection(headAdapter.getPosition(expense.getExpenseHead()));
            spinnerCategory.setSelection(categoryAdapter.getPosition(expense.getExpenseCategory()));
            tieExpenseDate.setText(expense.getDate());
            tieExpenseDetails.setText(expense.getDetails());
            Log.i("ExpenseDetailsActivity","Currency Position"+String.valueOf(getPositionOfCurrency(expense.getCurrency())));
            spinnerCurrency.setSelection(getPositionOfCurrency(expense.getCurrency()));
            switchRecurring.setChecked(expense.getRecurring());
            setTitleOfActionBar(getString(R.string.edit_expense));
            amountSeekBar.setProgress((int) (expense.getAmount() / 100));
            addAttachment(expense.getAttachemnts());
            setFieldsEditable(isEditable);
            btnSave.setText(R.string.update);
            if (!isEditable) {
                layoutBtmButtons.setVisibility(View.GONE);
                setTitleOfActionBar(getString(R.string.view_expense));
            }
            tieExpenseDetails.clearFocus();



        } else {
            setTitleOfActionBar(getString(R.string.add_expense));
            if (menu != null) {
                menu.findItem(R.id.view_edit_btn).setVisible(false);
                menu.findItem(R.id.action_delete).setVisible(false);
            }
            tieExpenseDate.setText(sdf.format(new Date()));
        }
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.attachement_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        btnAttachCamera = sheetView.findViewById(R.id.btn_attach_camera);
        btnAttachCancel = sheetView.findViewById(R.id.btn_attach_cancel);
        btnAttachDoc = sheetView.findViewById(R.id.btn_attach_document);
        btnAttachDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
        btnAttachCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        btnAttachCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePictureIntent();
            }
        });
        addOnClickListenerForAmountField();

        setUpAnimation();
        tieExpenseAmount.clearFocus();

        spinnerHead.requestFocus();
        tieExpenseHead.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            spinnerHead.requestPointerCapture();
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.expense_details, menu);
        if (expense != null) {
            menu.findItem(R.id.view_edit_btn).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(true);
        } else {
            menu.findItem(R.id.view_edit_btn).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return true;
    }

    private void setTitleOfActionBar(String titleText) {
        if (getActionBar() != null) {
            getActionBar().setTitle(titleText);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleText);
        }
    }

    private void setUpAnimation() {

    }

    private void setFieldsEditable(boolean isEditable) {
        tieExpenseDetails.setEnabled(isEditable);
        tieExpenseDate.setEnabled(isEditable);
        tieExpenseHead.setEnabled(isEditable);
        tieExpenseAmount.setEnabled(isEditable);
        tieExpenseAmount.setClickable(isEditable);
        spinnerCurrency.setEnabled(isEditable);
        tieCategory.setEnabled(isEditable);
        switchRecurring.setEnabled(isEditable);
        //spinner.setEnabled(isEditable);
        seekBarConstraintLayout.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        layoutBtmButtons.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        btnCapturePicture.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        txtRemoveAttachment.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        spinnerCategory.setEnabled(isEditable);
        spinnerHead.setEnabled(isEditable);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteExpense();
                return true;
            case R.id.view_edit_btn: {
                isEditable = true;
                setFieldsEditable(true);
                btnSave.setText(R.string.update);
                setTitleOfActionBar(getString(R.string.edit_expense));
                menu.findItem(R.id.view_edit_btn).setVisible(false);
                menu.findItem(R.id.action_delete).setVisible(false);
                return true;

            }
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void deleteExpense() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        expenseDetailViewModel.deleteExpense(expense);


                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void returnToMainActivity() {
        onBackPressed();
    }

    public void openInBrowser() {

    }

    @OnClick(R.id.add_date)
    public void setExpenseDate() {

        datePickerDialog = new DatePickerDialog(this, R.style.yourCustomStyle, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    @OnClick(R.id.add_attach_btn)
    public void showAttachementDialogue(){
        //tieExpenseAmount.clearFocus();
        mBottomSheetDialog.show();
    }


    public void sendTakePictureIntent() {
        new AlertDialog.Builder(this)
                .setTitle("Add Attachment")
                .setMessage("One attachment is allowed per Expense")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            File pictureFile = null;
                            try {
                                pictureFile = getPictureFile();
                            } catch (IOException ex) {
                                showSnackbar("Photo file can't be created, please try again");

                                return;
                            }
                            if (pictureFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(ExpenseDetailActivity.this,
                                        "app.perfecto.com.expencemanager.fileprovider",
                                        pictureFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE);
                            }
                        }


                    }
                })
                .show();


    }

    /*@OnClick(R.id.img_btn_attach_file)*/
    public void openFilePicker() {

        new AlertDialog.Builder(this)
                .setTitle("Add Attachment")
                .setMessage("One attachment is allowed per Expense")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (ContextCompat.checkSelfPermission(ExpenseDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ExpenseDetailActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    READ_STORAGE);

                        } else {
                            showFilePicker();
                        }
                    }
                })
                .show();


    }

    @OnClick(R.id.add_save_btn)
    public void addNewExpense() {
        if (!isEditable) {
            expense = new Expense();
            updateExpenseDetailsFromForm();
            expenseDetailViewModel.addNewExpense(expense);
        } else {
            updateExpenseDetailsFromForm();
            expenseDetailViewModel.updateExpense(expense);
        }

    }

    private void updateExpenseDetailsFromForm() {
        String head = tieExpenseHead.getText().toString();
        String date = tieExpenseDate.getText().toString();
        String category = tieCategory.getText().toString();
        String currency = spinnerCurrency.getSelectedItem().toString();
        String details = tieExpenseDetails.getText().toString();
        expense.setDate(date);
        expense.setExpenseHead(head);
        expense.setDetails(details);
        expense.setExpenseCategory(category);
        expense.setCurrency(currency);
        expense.setRecurring(switchRecurring.isChecked());
        AppLogger.i("Amount :", tieExpenseAmount.getText());
        expense.setAmount(Double.parseDouble(tieExpenseAmount.getText().toString()));
        if (attachments != null) {
            if (attachments.size() > 0) {
                expense.setAttachemnts(attachments.get(0));
            }else{
                expense.setAttachemnts("");
            }
        }else{
            expense.setAttachemnts("");
        }


    }

    private void addTextChangeListner(){
        tieExpenseAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tieExpenseAmount.hasFocus()) {
                    String value = s.toString();
                    if(value.length()>0) {
                        int intValue = (((int)Float.parseFloat(value))/ 100);
                        amountSeekBar.setProgress(intValue);
                        //tieExpenseAmount.setText(String.format("%.2f",Float.parseFloat(value)));
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE_IMAGE: {
                    File imgFile = new File(pictureFilePath);
                    if (imgFile.exists()) {
                        AppLogger.i("ImagePath:" + imgFile.getAbsolutePath());
                        addAttachment(imgFile.getAbsolutePath());
                        mBottomSheetDialog.dismiss();
                    }
                    break;
                }
                case 7: {
                    if (data.getData().getPath() != null) {
                        Uri filePath = data.getData();
                        String fullPath = Commons.getPath(filePath, this);
                        addAttachment(fullPath);
                    } else {
                        AppLogger.i(this.getLocalClassName(), "Null file path");
                        mBottomSheetDialog.dismiss();
                    }
                    break;
                }
            }

        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "Expense_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    private void addAttachment(String filePath) {
        attachments.clear();
        if (!TextUtils.isEmpty(filePath)) {
            attachments.add(filePath);
        }
        if (attachments.size() > 0) {
            if (TextUtils.isEmpty(attachments.get(0))) {
                txtRemoveAttachment.setEnabled(false);
                tvViewAttachment.setEnabled(false);
            } else {
                tvViewAttachment.setVisibility(View.VISIBLE);
                txtRemoveAttachment.setVisibility(View.VISIBLE);
                txtRemoveAttachment.setEnabled(true);
                tvViewAttachment.setEnabled(true);
            }
        } else {
            txtRemoveAttachment.setEnabled(false);
            tvViewAttachment.setEnabled(false);
        }
        tvAttachments.setText("Attachments(" + String.valueOf(attachments.size()) + ")");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (dialog != null) {   //Show dialog if the read permission has been granted.
                        showFilePicker();
                    }
                } else {
                    //Permission has not been granted. Notify the user.
                    showSnackbar("Permission is Required for getting list of files");
                    //Toast.makeText(this, "Permission is Required for getting list of files", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 7);
    }

    private void resetFields() {
        tieExpenseHead.setText("");
        tieExpenseAmount.setText(getString(R.string.default_amount));
        tieExpenseDetails.setText("");
        tieExpenseDate.setText("");
        //tieCategory.setText("");
        //spinner.setSelection(new ArrayList<>());
        expenseDetailViewModel.getDefaultCurrency();
        attachments.clear();
        addAttachment(null);
    }

    @OnClick(R.id.view_view_attach_btn)
    public void viewAttachments() {
        if (attachments != null) {
            if (attachments.size() > 0) {
                String path = attachments.get(0);
                Log.i("Files:", path);
                //Uri uri = Uri.fromFile(new File(path));
                Uri uri = FileProvider.getUriForFile(this, "app.perfecto.com.expencemanager.fileprovider", new File(path));
                Log.i("Uri path:", uri.getPath());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                PackageManager pm = this.getPackageManager();
                if (intent.resolveActivity(pm) != null) {
                    startActivity(intent);
                }

            }
        }
    }

    @OnClick(R.id.removeAttachment)
    public void removeAttachment() {
        if (attachments != null) {
            if (attachments.size() > 0) {
                attachments.clear();
                addAttachment(null);
            }
        }
    }

    @OnClick(R.id.tie_expense_head)
    public void showExpense(){
        spinnerHead.setVisibility(View.VISIBLE);
        spinnerHead.performClick();
    }

    @Override
    protected void onResume() {
        if(mBottomSheetDialog != null){
            mBottomSheetDialog.dismiss();
        }
        super.onResume();
    }

    private int getPositionOfCurrency(String currency){
        int pos =-1;
        String currencyPart = currency.split("-")[0];
        if(currencyAdapter!= null){
            if(!currencyAdapter.isEmpty()){
              for(int i=0;i< currencyAdapter.getCount();i++){
                  if(Objects.requireNonNull(currencyAdapter.getItem(i)).contains(currencyPart)){
                      return i;
                  }
              }
            }
        }
        return pos;
    }

    private void addOnClickListenerForAmountField(){
       tieExpenseAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus){
                   String amount = tieExpenseAmount.getEditableText().toString();
                   if(amount.length()>0){
                       float amountF = Float.parseFloat(amount);
                       if(amountF == 0f){
                           tieExpenseAmount.setText("");
                       }
                   }
               }else {
                   String amount = tieExpenseAmount.getEditableText().toString();
                   if(amount.length()>0){
                       float amountF = Float.parseFloat(amount);
                       if(amountF == 0f){
                           tieExpenseAmount.setText("0.00");
                       }else{
                           tieExpenseAmount.setText(String.format("%.2f",amountF));
                       }
                   }else{
                       tieExpenseAmount.setText("0.00");
                   }
               }
           }
       });
    }
}

