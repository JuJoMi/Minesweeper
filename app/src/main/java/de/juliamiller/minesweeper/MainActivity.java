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

    private boolean[] bomben = new boolean[25];     //Spielfelder 5x5
    private int counter = 20;   //Anzahl freie nicht-geklickte Felder
    private int flag = 5;   //Anzahl setzbare Flaggen
    private boolean state = true;   //Spiel aktiv; Smiley

    private void SetBomben(){
        //Alle Spielfelder auf "leer" setzen
        for (int i = 0; i < 25; i++)
        {
            bomben[i] = false;
        }

        //5 Bomben zufällig verteilen
        for (int i = 0; i < 5; i++)
        {
            int random = new Random().nextInt(25); //Zufallszahl zwischen (einschließlich)0 und (ausschließlich)25
            if(bomben[random] == true){
                i--;
            }
            else{
                bomben[random] = true;
                //Entsprechendes Textfeld benennen, ID festlegen und Minenbild setzen
                String txtID = "txt" + String.format("%02d", random);
                int resID = getResources().getIdentifier(txtID, "id", getPackageName());
                TextView textview = (TextView) findViewById(resID);
                textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mine,0,0,0);
            }
        }
    }

    //Je Feld umliegende Bomben zählen, um entsprechendes Textfeld mit Zahl zu versehen
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

        Init(); //Initialisierung wenn die App gestartet wird

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!state)
                {
                    Init(); // Initialisierung wenn der NewGame-Button gedrückt wird
                    btnNewGame.setImageResource(R.drawable.smiley_neutral);
                    state = true;
                }
                else
                {
                    state = false;
                }
            }
        });

        //"Gedrückthalten" für jeden Button implementieren
        for (int x = 0; x < 25; x++) {
            String btnID = "btn" + String.format("%02d", x);
            int resID = getResources().getIdentifier(btnID, "id", getPackageName());
            final ImageButton imageButton = (ImageButton) findViewById(resID);

            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (imageButton.getTag() == "1") {
                        imageButton.setImageResource(R.drawable.tile);
                        imageButton.setTag("2");
                        flag++;
                    }
                    else {
                        imageButton.setImageResource(R.drawable.tile_flag);
                        imageButton.setTag("1");
                        flag--;
                    }
                    //Anzahl Flaggen oben links
                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setText(String.valueOf(flag));
                    return true;
                }
            });
        }
    }

    public void Init(){
        counter = 20;
        SetBomben(); //Funktionsaufruf: Bomben zufällig auf Felder verteilen TODO: erstes geklicktes Feld ist keine Bombe
        //untere Ebene Textfelder beschriften
        for(int i = 0; i < 25; i++) {
            String txtID = "txt" + String.format("%02d", i);
            int resID = getResources().getIdentifier(txtID, "id", getPackageName());
            TextView textview = (TextView) findViewById(resID);
            int anz = CountBomben(i); //Funktionsaufruf: Wie viele Bomben umgeben ein bestimmtes Feld?
            switch (anz) {
                case 0:
                    textview.setText("");
                    break;
                case 1:
                    textview.setText(String.valueOf(anz)); //Anzahl als Text einfügen
                    textview.setTextColor(Color.parseColor("#FF00FF")); //Text färben
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
            textview.setVisibility(textview.INVISIBLE); //Textfelder verstecken
            if (!bomben[i]) { //wenn ein Feld keine Bombe hat, wird das Image des Textfeldes gelöscht
                textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            //obere Ebene Buttons einrichten
            ImageButton imageButton = (ImageButton) findViewById(getResources().getIdentifier("btn" + String.format("%02d", i), "id", getPackageName()));
            imageButton.setImageResource(R.drawable.tile);
            imageButton.setTag("2");
            imageButton.setVisibility(ImageButton.VISIBLE);
            imageButton.setEnabled(true);
            flag = 5; //noch setzbare Flaggen
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.valueOf(flag)); //Anzahl Flaggen oben links
        }
    }


    public void actClick(View view) {
        String btnID = view.getResources().getResourceEntryName(view.getId());
        int ID = Integer.parseInt(btnID.substring(3,5));
        int resID = getResources().getIdentifier(btnID, "id", getPackageName());
        ImageButton imageButton = (ImageButton) findViewById(resID);
        if (imageButton.getTag() == "2") { //angeklicktes Feld ist keine Flagge
            if (bomben[ID]) { //angeklicktes Feld ist eine Bombe
                GameOver(ID);
            } else { //Button deaktivieren und in den Hintergrund; Textview mit Zahl in den Vordergrund
                resID = getResources().getIdentifier("txt" + String.format("%02d", ID), "id", getPackageName());
                TextView textview = (TextView) findViewById(resID);
                textview.setVisibility(textview.VISIBLE);
                ImageButton btn = (ImageButton) findViewById(view.getId());
                btn.setVisibility(view.INVISIBLE);
                btn.setEnabled(false);
                counter--;
                if (counter == 0) { //alle "ungefährlichen" Felder wurden angeklickt
                    GameWin();
                }
            }
        }
    }

    private void GameOver(int ID) {
        Log.d("UI", "Loooser"); //Ausgabe im Log zur Kontrolle in der Testphase

        //anpassen des Smileys
        ImageButton btnNewGame = (ImageButton) findViewById(R.id.btnNewGame);
        btnNewGame.setImageResource(R.drawable.smiley_sad);

        for(int k = 0; k <= 24; k++) { //alle Buttons deaktivieren
                String btnID = "btn" + String.format("%02d", k);
                int resID = getResources().getIdentifier(btnID, "id", getPackageName());
                ImageButton imageButton = (ImageButton) findViewById(resID);
                imageButton.setEnabled(false);
            if (bomben[k] == true) {
                imageButton.setVisibility(imageButton.INVISIBLE); //in den Hintergrund, wenn Bombe
                String txtID = "txt" + String.format("%02d", k);
                resID = getResources().getIdentifier(txtID, "id", getPackageName());
                TextView textview = (TextView) findViewById(resID);
                if ( k == ID) { //anpassen des Images der Textview des angeklickten Buttons; in den Vordergrund
                    textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mine_broken, 0, 0, 0);
                }
                textview.setVisibility(textview.VISIBLE);
            }
        }
    }

    private void GameWin() {
        Log.d("UI", "Wiiiiiiin");  //Ausgabe im Log zur Kontrolle in der Testphase

        //anpassen des Smileys
        ImageButton imageButton = findViewById(R.id.btnNewGame);
        imageButton.setImageResource(R.drawable.smiley);

        for(int k = 0; k <= 24; k++) { //alle Buttons deaktivieren
            String btnID = "btn" + String.format("%02d", k);
            int resID = getResources().getIdentifier(btnID, "id", getPackageName());
            imageButton = (ImageButton) findViewById(resID);
            imageButton.setEnabled(false);
            if (bomben[k] == true) {
                imageButton.setVisibility(imageButton.INVISIBLE); //in den Hintergrund, wenn Bombe
                String txtID = "txt" + String.format("%02d", k);
                resID = getResources().getIdentifier(txtID, "id", getPackageName());
                TextView textview = (TextView) findViewById(resID);
                //statt Bomben Flaggen als Image setzen
                textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tile_flag,0,0,0);
                textview.setVisibility(textview.VISIBLE); //in den Vordergrund
            }
        }
    }
}
