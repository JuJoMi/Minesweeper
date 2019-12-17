package de.juliamiller.minesweeper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean[] bomben = new boolean[25];
    private int counter = 20;

    boolean state = true;

    private void SetBomben(){
        for (int i = 0; i < 25; i++)
        {
            bomben[i] = false;
        }

        for (int i = 0; i < 5; i++)
        {
            int random = new Random().nextInt(25);
            if(bomben[random] == true){
                i--;
            }
            else{
                bomben[random] = true;
                String txtID = "txt" + String.format("%02d", random);
                int resID = getResources().getIdentifier(txtID, "id", getPackageName());
                TextView textview = (TextView) findViewById(resID);
                textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mine,0,0,0);
            }
        }
    }

    private int CountBomben(int feld){
        int anzahlBomben = 0;

        if(feld >= 5){ //erste reihe filtern
            if(feld <= 19){ //letzte reihe filtern
                if(feld % 5 == 0){  //linke reihe filtern
                    if(bomben[feld - 4]){ anzahlBomben++; }
                    if(bomben[feld + 1]){ anzahlBomben++; }
                    if(bomben[feld + 6]){ anzahlBomben++; }
                }
                else if(feld % 5 == 4){ //rechte reihe filtern
                    if(bomben[feld - 6]){ anzahlBomben++; }
                    if(bomben[feld - 1]){ anzahlBomben++; }
                    if(bomben[feld + 4]){ anzahlBomben++; }
                }
                else{ //mittlere elemente
                    if(bomben[feld - 6]){ anzahlBomben++; }
                    if(bomben[feld - 4]){ anzahlBomben++; }
                    if(bomben[feld - 1]){ anzahlBomben++; }
                    if(bomben[feld + 1]){ anzahlBomben++; }
                    if(bomben[feld + 4]){ anzahlBomben++; }
                    if(bomben[feld + 6]){ anzahlBomben++; }
                }
                if(bomben[feld + 5]){ anzahlBomben++; }
                if(bomben[feld - 5]){ anzahlBomben++; }
            }
            else{//letzte reihe
                if (feld == 20){
                    if(bomben[feld - 4]){ anzahlBomben++; }
                    if(bomben[feld + 1]){ anzahlBomben++; }
                }
                else if (feld == 24){
                    if(bomben[feld - 6]){ anzahlBomben++; }
                    if(bomben[feld - 1]){ anzahlBomben++; }
                }
                else{
                    if(bomben[feld - 6]){ anzahlBomben++; }
                    if(bomben[feld - 4]){ anzahlBomben++; }
                    if(bomben[feld - 1]){ anzahlBomben++; }
                    if(bomben[feld + 1]){ anzahlBomben++; }
                }
                if(bomben[feld - 5]){ anzahlBomben++; }
            }
        }
        else{//erste reihe
            if(feld == 0){
                if(bomben[feld + 1]){ anzahlBomben++; }
                if(bomben[feld + 6]){ anzahlBomben++; }
            }
            else if(feld == 4){
                if(bomben[feld - 1]){ anzahlBomben++; }
                if(bomben[feld + 4]){ anzahlBomben++; }
            }
            else{
                if(bomben[feld - 1]){ anzahlBomben++; }
                if(bomben[feld + 1]){ anzahlBomben++; }
                if(bomben[feld + 4]){ anzahlBomben++; }
                if(bomben[feld + 6]){ anzahlBomben++; }
            }
            if(bomben[feld + 5]){ anzahlBomben++; }
        }
        return anzahlBomben;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton btnNewGame = (ImageButton) findViewById(R.id.btnNewGame);

        Init();

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!state)
                {
                    Init();
                    btnNewGame.setImageResource(R.drawable.smiley_neutral);
                    state = true;
                }
                else
                {
                    state = false;
                }
            }
        });
    }

    public void Init(){
        counter = 20;
        SetBomben();
        for(int i = 0; i < 25; i++) {
            String txtID = "txt" + String.format("%02d", i);
            int resID = getResources().getIdentifier(txtID, "id", getPackageName());
            TextView textview = (TextView) findViewById(resID);
            int anz = CountBomben(i);
            switch (anz) {
                case 0:
                    textview.setText("");
                    break;
                case 1:
                    textview.setText(String.valueOf(anz));
                    textview.setTextColor(Color.parseColor("#FF00FF"));
                    break;
                case 2:
                    textview.setText(String.valueOf(anz));
                    textview.setTextColor(Color.parseColor("#00FF00"));
                    break;
                case 3:
                    textview.setText(String.valueOf(anz));
                    textview.setTextColor(Color.parseColor("#FF0000"));
                    break;
                case 4:
                    textview.setText(String.valueOf(anz));
                    textview.setTextColor(Color.parseColor("#0000FF"));
                    break;
                case 5:
                    textview.setText(String.valueOf(anz));
                    textview.setTextColor(Color.parseColor("#FFFF00"));
                    break;
            }
            textview.setVisibility(textview.INVISIBLE);
            if (!bomben[i]) {
                textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            ImageButton imageButton = (ImageButton) findViewById(getResources().getIdentifier("btn" + String.format("%02d", i), "id", getPackageName()));
            imageButton.setVisibility(ImageButton.VISIBLE);
            imageButton.setEnabled(true);
        }
    }

    //TODO: OnLongClickListener erstellen für Fahne setzen
    /*public void onLongClick(View view) {
        String btnID = view.getResources().getResourceEntryName(view.getId());
        int ID = Integer.parseInt(btnID.substring(3,5));
        //ImageButton imageButton = (ImageButton) findViewById()

    }*/

    public void actClick(View view) {
        String btnID = view.getResources().getResourceEntryName(view.getId());
        int ID = Integer.parseInt(btnID.substring(3,5));

        if (bomben[ID])
        {
            GameOver(ID);
        }
        else
        {
            int resID = getResources().getIdentifier("txt" + String.format("%02d", ID), "id", getPackageName());
            TextView textview = (TextView) findViewById(resID);
            textview.setVisibility(textview.VISIBLE);
            ImageButton btn = (ImageButton) findViewById(view.getId());
            btn.setVisibility(view.INVISIBLE);
            btn.setEnabled(false);
            counter--;
            if(counter == 0) {
                GameWin();
            }

            //TODO: Textfeld muss auf Bombenzählen (Fahnen setzen)
        }
    }

    private void GameOver(int ID) {
        Log.d("UI", "Loooser");

        String txtID = "txt" + String.format("%02d", ID);
        int resID = getResources().getIdentifier(txtID, "id", getPackageName());
        TextView textview = (TextView) findViewById(resID);
        textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mine_broken,0,0,0);
        textview.setVisibility(textview.VISIBLE);

        String btnID = "btn" + String.format("%02d", ID);
        resID = getResources().getIdentifier(btnID, "id", getPackageName());
        ImageButton imageButton = (ImageButton) findViewById(resID);
        imageButton.setEnabled(false);
        imageButton.setVisibility(imageButton.INVISIBLE);

        ImageButton btnNewGame = (ImageButton) findViewById(R.id.btnNewGame);
        btnNewGame.setImageResource(R.drawable.smiley_sad);

        for(int k = 0; k <= 24; k++) {
                btnID = "btn" + String.format("%02d", k);
                resID = getResources().getIdentifier(btnID, "id", getPackageName());
                imageButton = (ImageButton) findViewById(resID);
                imageButton.setEnabled(false);
            if (bomben[k] == true) {
                imageButton.setVisibility(imageButton.INVISIBLE);
                txtID = "txt" + String.format("%02d", k);
                resID = getResources().getIdentifier(txtID, "id", getPackageName());
                textview = (TextView) findViewById(resID);
                textview.setVisibility(textview.VISIBLE);
            }
        }
    }

    private void GameWin() {
        Log.d("UI", "Wiiiiiiin");
        ImageButton imageButton = findViewById(R.id.btnNewGame);
        imageButton.setImageResource(R.drawable.smiley_smile);

        //TODO: Bomben Button disablen, Fahne anzeigen
        for(int k = 0; k <= 24; k++) {
            String btnID = "btn" + String.format("%02d", k);
            int resID = getResources().getIdentifier(btnID, "id", getPackageName());
            imageButton = (ImageButton) findViewById(resID);
            imageButton.setEnabled(false);
            if (bomben[k] == true) {
                imageButton.setVisibility(imageButton.INVISIBLE);
                String txtID = "txt" + String.format("%02d", k);
                resID = getResources().getIdentifier(txtID, "id", getPackageName());
                TextView textview = (TextView) findViewById(resID);
                textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tile_flag,0,0,0);
                textview.setVisibility(textview.VISIBLE);
            }
        }
    }
}
