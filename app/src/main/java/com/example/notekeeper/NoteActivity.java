package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_POSITION = "NOTE_POSITION";
    public static final String ORIGNAL_NOTE_COURSE_ID = "ORGINAL_NOTE_COURSE_ID";
    public static final String ORIGNAL_NOTE_TITLE = "ORGINAL_NOTE_TITLE";
    public static final String ORIGNAL_NOTE_TEXT = "ORGINAL_NOTE_TEXT";
    private int defaultValue = -1;
    private int position;
    private boolean isNewNote;
    private NoteInfo note;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int notePosition;
    private boolean isCancel;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = findViewById(R.id.spinner_Courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapterCourses);

        textNoteTitle = findViewById(R.id.text_note_title);
        textNoteText = findViewById(R.id.text_note_text);

        readDisplayStatValue();
        if(savedInstanceState==null)
        {
            saveOriginalNoteValue();
        }
        else
        {
            restoreOriginalNoteValue(savedInstanceState);
        }
        if (isNewNote) {
            createNewNote();
        } else {
            displayNote(spinnerCourses, textNoteTitle, textNoteText);
        }
    }

    private void restoreOriginalNoteValue(Bundle savedInstanceState) {
        originalNoteCourseId = savedInstanceState.getString(ORIGNAL_NOTE_COURSE_ID);
        originalNoteTitle = savedInstanceState.getString(ORIGNAL_NOTE_TITLE);
        originalNoteText = savedInstanceState.getString(ORIGNAL_NOTE_TEXT);
    }

    private void saveOriginalNoteValue() {
        if (isNewNote)
            return;
        originalNoteCourseId = note.getCourse().getCourseId();
        originalNoteTitle = note.getTitle();
        originalNoteText = note.getText();
    }

    private void createNewNote() {

        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote();
        note = dm.getNotes().get(notePosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCancel) {
            if (isNewNote) {
                DataManager.getInstance().removeNote(notePosition);
            } else {
                storePreviousNoteValue();
            }
        } else {
            saveNote();
        }
    }

    private void storePreviousNoteValue() {
        CourseInfo course = DataManager.getInstance().getCourse(originalNoteCourseId);
        note.setCourse(course);
        note.setTitle(originalNoteTitle);
        note.setText(originalNoteText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGNAL_NOTE_COURSE_ID,originalNoteCourseId);
        outState.putString(ORIGNAL_NOTE_TITLE,originalNoteTitle);
        outState.putString(ORIGNAL_NOTE_TEXT,originalNoteText);
    }

    private void saveNote() {
        note.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        note.setTitle(textNoteTitle.getText().toString());
        note.setText(textNoteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(note.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(note.getTitle());
        textNoteText.setText(note.getText());
    }

    private void readDisplayStatValue() {
        Intent intent = getIntent();
        position = intent.getIntExtra(NOTE_POSITION, defaultValue);
        isNewNote = position == defaultValue;

        if (!isNewNote) {
            note = DataManager.getInstance().getNotes().get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendMail();
            return true;
        } else if (id == R.id.action_cancel) {
            isCancel = true;
            finish();
        }
        else if (id == R.id.acrion_next)
        {
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.acrion_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(notePosition < lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();
        ++notePosition;
        note = DataManager.getInstance().getNotes().get(notePosition);
        saveOriginalNoteValue();
        displayNote(spinnerCourses,textNoteTitle,textNoteText);
        invalidateOptionsMenu();
    }

    private void sendMail() {
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = textNoteTitle.getText().toString();
        String text = course.getTitle() + " " + textNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
