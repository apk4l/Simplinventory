package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Hashtable;
import java.util.HashMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

public class FoodStockActivity extends AppCompatActivity {
    private EditText mDoughEditText;
    private EditText mCheeseEditText;
    private EditText mSauceEditText;
    private EditText mChickenEditText;
    private EditText mSausageEditText;
    private EditText mPepperoniEditText;
    private EditText mBurgerEditText;
    private EditText mBaconEditText;
    private EditText mHamEditText;
    private EditText mSteakEditText;
    private EditText mMushroomsEditText;
    private EditText mBlackOlivesEditText;
    private EditText mGreenPeppersEditText;
    private EditText mOnionsEditText;
    private EditText mBananaPeppersEditText;
    private EditText mJalapenosEditText;
    private EditText mRanchEditText;
    private EditText mBuffaloSauceEditText;
    private EditText mBbqSauceEditText;
    private EditText mAlfredoMixEditText;
    private EditText m14inchEditText;
    private EditText m8inchEditText;
    private EditText mOilEditText;
    private EditText mGButterEditText;
    private EditText mPorkEditText;
    private EditText mCornmealEditText;
    private EditText mPineappleEditText;
    private EditText mPrepEditText;
    private EditText mLidsEditText;
    private EditText mSanitabsEditText;
    private EditText mSeasoningEditText;
    private TextView mOrderTextView;
    private Button mSubmitButton;

    // Required stock
    private static final int REQUIRED_DOUGH = 90;
    private static final int DOUGH_PER_CASE = 30;
    private static final int REQUIRED_CHEESE = 12;
    private static final int CHEESE_PER_CASE = 4;
    private static final int REQUIRED_SAUCE = 4;
    private static final int SAUCE_PER_CASE = 6;
    private static final int REQUIRED_CHICKEN = 4;
    private static final int CHICKEN_PER_CASE = 6;
    private static final int REQUIRED_SAUSAGE = 4;
    private static final int SAUSAGE_PER_CASE = 6;
    private static final int REQUIRED_PEPPERONI = 3;
    private static final int PEPPERONI_PER_CASE = 2;
    private static final int REQUIRED_BURGER = 2;
    private static final int BURGER_PER_CASE = 2;
    private static final int REQUIRED_BACON = 2;
    private static final int BACON_PER_CASE = 2;
    private static final int REQUIRED_HAM = 2;
    private static final int HAM_PER_CASE = 2;
    private static final int REQUIRED_STEAK = 1;
    private static final int STEAK_PER_CASE = 1;
    private static final int REQUIRED_MUSHROOMS = 3;
    private static final int MUSHROOMS_PER_CASE = 6;
    private static final int REQUIRED_BLACKOLIVES = 3;
    private static final int BLACKOLIVES_PER_CASE = 6;
    private static final int REQUIRED_GREENPEPPERS = 4;
    private static final int GREENPEPPERS_PER_CASE = 8;
    private static final int REQUIRED_ONIONS = 2;
    private static final int REQUIRED_BANANAPEPPERS = 2;
    private static final int BANANAPEPPERS_PER_CASE = 4;
    private static final int REQUIRED_JALAPENOS = 2;
    private static final int JALAPENOS_PER_CASE = 4;
    private static final int REQUIRED_RANCH = 2;
    private static final int RANCH_PER_CASE = 4;
    private static final int REQUIRED_BUFFALO = 2;
    private static final int BUFFALO_PER_CASE = 4;
    private static final int REQUIRED_BBQ = 2;
    private static final int BBQ_PER_CASE = 4;
    private static final int REQUIRED_ALFREDO = 3;
    private static final int ALFREDO_PER_CASE = 6;
    private static final int REQUIRED_14INCH = 30;
    private static final int INCH14_PER_CASE = 50;
    private static final int REQUIRED_8INCH = 30;
    private static final int INCH8_PER_CASE = 50;
    private static final int REQUIRED_OIL = 2;
    private static final int REQUIRED_GBUTTER = 2;
    private static final int REQUIRED_PORK = 2;
    private static final int REQUIRED_CORNMEAL = 1;
    private static final int REQUIRED_PINEAPPLE = 1;
    private static final int REQUIRED_PREP = 8;
    private static final int PREP_PER_CASE = 7;
    private static final int REQUIRED_LIDS = 8;
    private static final int LIDS_PER_CASE = 7;
    private static final int REQUIRED_SANITABS = 1;
    private static final int REQUIRED_SEASONING = 2;

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.navigation.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stock:
                // Handle "Food Stock" menu item click
                return true;
            case R.id.menu_history:
                // Handle "Order History" menu item click
                Intent intent = new Intent(FoodStockActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.login:
                // Handle "Order History" menu item click
                intent = new Intent(FoodStockActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.new_user:
                // Handle "Order History" menu item click
                intent = new Intent(FoodStockActivity.this, NewUserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stock);

        mDoughEditText = findViewById(R.id.dough_edit_text);
        mCheeseEditText = findViewById(R.id.cheese_edit_text);
        mSauceEditText = findViewById(R.id.sauce_edit_text);
        mChickenEditText = findViewById(R.id.chicken_edit_text);
        mSausageEditText = findViewById(R.id.sausage_edit_text);
        mPepperoniEditText = findViewById(R.id.pepperoni_edit_text);
        mBurgerEditText = findViewById(R.id.burger_edit_text);
        mBaconEditText = findViewById(R.id.bacon_edit_text);
        mHamEditText = findViewById(R.id.ham_edit_text);
        mSteakEditText = findViewById(R.id.steak_edit_text);
        mMushroomsEditText = findViewById(R.id.mushrooms_edit_text);
        mBlackOlivesEditText = findViewById(R.id.blackolives_edit_text);
        mGreenPeppersEditText = findViewById(R.id.greenpeppers_edit_text);
        mOnionsEditText = findViewById(R.id.onions_edit_text);
        mBananaPeppersEditText = findViewById(R.id.bananapeppers_edit_text);
        mJalapenosEditText = findViewById(R.id.jalapenos_edit_text);
        mRanchEditText = findViewById(R.id.ranch_edit_text);
        mBuffaloSauceEditText = findViewById(R.id.buffalo_edit_text);
        mBbqSauceEditText = findViewById(R.id.bbq_edit_text);
        mAlfredoMixEditText = findViewById(R.id.alfredo_edit_text);
        m14inchEditText = findViewById(R.id.inch14_edit_text);
        m8inchEditText = findViewById(R.id.inch8_edit_text);
        mOilEditText = findViewById(R.id.oil_edit_text);
        mGButterEditText = findViewById(R.id.gbutter_edit_text);
        mPorkEditText = findViewById(R.id.pork_edit_text);
        mCornmealEditText = findViewById(R.id.cornmeal_edit_text);
        mPineappleEditText = findViewById(R.id.pineapple_edit_text);
        mPrepEditText = findViewById(R.id.prep_edit_text);
        mLidsEditText = findViewById(R.id.lids_edit_text);
        mSanitabsEditText = findViewById(R.id.sanitabs_edit_text);
        mSeasoningEditText = findViewById(R.id.seasoning_edit_text);
        mOrderTextView = findViewById(R.id.order_text_view);
        mSubmitButton = findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current stock
                int currentDough = Integer.parseInt(mDoughEditText.getText().toString());
                int currentCheese = Integer.parseInt(mCheeseEditText.getText().toString());
                int currentSauce = Integer.parseInt(mSauceEditText.getText().toString());
                int currentChicken = Integer.parseInt(mChickenEditText.getText().toString());
                int currentSausage = Integer.parseInt(mSausageEditText.getText().toString());
                int currentPepperoni = Integer.parseInt(mPepperoniEditText.getText().toString());
                int currentBurger = Integer.parseInt(mBurgerEditText.getText().toString());
                int currentBacon = Integer.parseInt(mBaconEditText.getText().toString());
                int currentHam = Integer.parseInt(mHamEditText.getText().toString());
                int currentSteak = Integer.parseInt(mSteakEditText.getText().toString());
                int currentMushrooms = Integer.parseInt(mMushroomsEditText.getText().toString());
                int currentBlackolives = Integer.parseInt(mBlackOlivesEditText.getText().toString());
                int currentGreenpeppers = Integer.parseInt(mGreenPeppersEditText.getText().toString());
                int currentOnions = Integer.parseInt(mOnionsEditText.getText().toString());
                int currentBananapeppers = Integer.parseInt(mBananaPeppersEditText.getText().toString());
                int currentJalapenos = Integer.parseInt(mJalapenosEditText.getText().toString());
                int currentRanch = Integer.parseInt(mRanchEditText.getText().toString());
                int currentBuffalo = Integer.parseInt(mBuffaloSauceEditText.getText().toString());
                int currentBbq = Integer.parseInt(mBbqSauceEditText.getText().toString());
                int currentAlfredo = Integer.parseInt(mAlfredoMixEditText.getText().toString());
                int current14inch = Integer.parseInt(m14inchEditText.getText().toString());
                int current8inch = Integer.parseInt(m8inchEditText.getText().toString());
                int currentOil = Integer.parseInt(mOilEditText.getText().toString());
                int currentGButter = Integer.parseInt(mGButterEditText.getText().toString());
                int currentPork = Integer.parseInt(mPorkEditText.getText().toString());
                int currentCornmeal = Integer.parseInt(mCornmealEditText.getText().toString());
                int currentPineapple = Integer.parseInt(mPineappleEditText.getText().toString());
                int currentPrep = Integer.parseInt(mPrepEditText.getText().toString());
                int currentLids = Integer.parseInt(mLidsEditText.getText().toString());
                int currentSanitabs = Integer.parseInt(mSanitabsEditText.getText().toString());
                int currentSeasoning = Integer.parseInt(mSeasoningEditText.getText().toString());

                // Compare current stock to required stock
                StringBuilder orders = new StringBuilder();
                if (currentDough < REQUIRED_DOUGH) {
                    int cases = (REQUIRED_DOUGH - currentDough) / DOUGH_PER_CASE;
                    if ((REQUIRED_DOUGH - currentDough) % DOUGH_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case dough \n");
                }
                if (currentCheese < REQUIRED_CHEESE) {
                    int cases = (REQUIRED_CHEESE - currentCheese) / CHEESE_PER_CASE;
                    if ((REQUIRED_CHEESE - currentCheese) % CHEESE_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case cheese \n");
                }
                if (currentSauce < REQUIRED_SAUCE) {
                    int cases = (REQUIRED_SAUCE - currentSauce) / SAUCE_PER_CASE;
                    if ((REQUIRED_SAUCE - currentSauce) % SAUCE_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case sauce \n");
                }
                if (currentChicken < REQUIRED_CHICKEN) {
                    int cases = (REQUIRED_CHICKEN - currentChicken) / CHICKEN_PER_CASE;
                    if ((REQUIRED_CHICKEN - currentChicken) % CHICKEN_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case chicken \n");
                }
                if (currentSausage < REQUIRED_SAUSAGE) {
                    int cases = (REQUIRED_SAUSAGE - currentSausage) / SAUSAGE_PER_CASE;
                    if ((REQUIRED_SAUSAGE - currentSausage) % SAUSAGE_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case sausage \n");
                }
                if (currentPepperoni < REQUIRED_PEPPERONI) {
                    int cases = (REQUIRED_PEPPERONI - currentPepperoni) / PEPPERONI_PER_CASE;
                    if ((REQUIRED_PEPPERONI - currentPepperoni) % PEPPERONI_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case pepperoni \n");
                }
                if (currentBurger < REQUIRED_BURGER) {
                    int cases = (REQUIRED_BURGER - currentBurger) / BURGER_PER_CASE;
                    if ((REQUIRED_BURGER - currentBurger) % BURGER_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case burger \n");
                }
                if (currentBacon < REQUIRED_BACON) {
                    int cases = (REQUIRED_BACON - currentBacon) / BACON_PER_CASE;
                    if ((REQUIRED_BACON - currentBacon) % BACON_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case bacon \n");
                }
                if (currentHam < REQUIRED_HAM) {
                    int cases = (REQUIRED_HAM - currentHam) / HAM_PER_CASE;
                    if ((REQUIRED_HAM - currentHam) % HAM_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case ham \n");
                }
                if (currentSteak < REQUIRED_STEAK) {
                    int cases = (REQUIRED_STEAK - currentSteak) / STEAK_PER_CASE;
                    if ((REQUIRED_STEAK - currentSteak) % STEAK_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case steak \n");
                }
                if (currentMushrooms < REQUIRED_MUSHROOMS) {
                    int cases = (REQUIRED_MUSHROOMS - currentMushrooms) / MUSHROOMS_PER_CASE;
                    if ((REQUIRED_MUSHROOMS - currentMushrooms) % MUSHROOMS_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case mushrooms \n");
                }
                if (currentBlackolives < REQUIRED_BLACKOLIVES) {
                    int cases = (REQUIRED_BLACKOLIVES - currentBlackolives) / BLACKOLIVES_PER_CASE;
                    if ((REQUIRED_BLACKOLIVES - currentBlackolives) % BLACKOLIVES_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case black olives \n");
                }
                if (currentGreenpeppers < REQUIRED_GREENPEPPERS) {
                    int cases = (REQUIRED_GREENPEPPERS - currentGreenpeppers) / GREENPEPPERS_PER_CASE;
                    if ((REQUIRED_GREENPEPPERS - currentGreenpeppers) % GREENPEPPERS_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case green peppers \n");
                }
                if (currentOnions < REQUIRED_ONIONS) {
                    int cases = REQUIRED_ONIONS - currentOnions;
                    if (REQUIRED_ONIONS - currentOnions != 0) {
                        ;
                    }
                    orders.append(cases + " 5lb bag onions \n");
                }
                if (currentBananapeppers < REQUIRED_BANANAPEPPERS) {
                    int cases = (REQUIRED_BANANAPEPPERS - currentBananapeppers) / BANANAPEPPERS_PER_CASE;
                    if ((REQUIRED_BANANAPEPPERS - currentBananapeppers) % BANANAPEPPERS_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case banana peppers \n");
                }
                if (currentJalapenos < REQUIRED_JALAPENOS) {
                    int cases = (REQUIRED_JALAPENOS - currentJalapenos) / JALAPENOS_PER_CASE;
                    if ((REQUIRED_JALAPENOS - currentJalapenos) % JALAPENOS_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case banana peppers \n");
                }
                if (currentRanch < REQUIRED_RANCH) {
                    int cases = (REQUIRED_RANCH - currentRanch) / RANCH_PER_CASE;
                    if ((REQUIRED_RANCH - currentRanch) % RANCH_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case ranch \n");
                }
                if (currentBuffalo < REQUIRED_BUFFALO) {
                    int cases = (REQUIRED_BUFFALO - currentBuffalo) / BUFFALO_PER_CASE;
                    if ((REQUIRED_BUFFALO - currentBuffalo) % BUFFALO_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case buffalo sauce \n");
                }
                if (currentBbq < REQUIRED_BBQ) {
                    int cases = (REQUIRED_BBQ - currentBbq) / BBQ_PER_CASE;
                    if ((REQUIRED_BBQ - currentBbq) % BBQ_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case bbq sauce \n");
                }
                if (currentAlfredo < REQUIRED_ALFREDO) {
                    int cases = (REQUIRED_ALFREDO - currentAlfredo) / ALFREDO_PER_CASE;
                    if ((REQUIRED_ALFREDO - currentAlfredo) % ALFREDO_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case alfredo mix \n");
                }
                if (current14inch < REQUIRED_14INCH) {
                    int cases = (REQUIRED_14INCH - current14inch) / INCH14_PER_CASE;
                    if ((REQUIRED_14INCH - current14inch) % INCH14_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case 14 inch boxes \n");
                }
                if (current8inch < REQUIRED_8INCH) {
                    int cases = (REQUIRED_8INCH - current8inch) / INCH8_PER_CASE;
                    if ((REQUIRED_8INCH - current8inch) % INCH8_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case 8 inch boxes \n");
                }
                if (currentOil < REQUIRED_OIL) {
                    int cases = REQUIRED_OIL - currentOil;
                    if (REQUIRED_OIL - currentOil != 0) {
                        ;
                    }
                    orders.append(cases + " oil jug \n");
                }
                if (currentGButter < REQUIRED_GBUTTER) {
                    int cases = REQUIRED_GBUTTER - currentGButter;
                    if (REQUIRED_GBUTTER - currentGButter != 0) {
                        ;
                    }
                    orders.append(cases + " jugs garlic butter \n \n GFS/Other:  \n");
                }
                if (currentPork < REQUIRED_PORK) {
                    int cases = REQUIRED_PORK - currentPork;
                    if (REQUIRED_PORK - currentPork != 0) {
                        ;
                    }
                    orders.append(cases + " pulled pork tray (GFS) \n");
                }
                if (currentCornmeal < REQUIRED_CORNMEAL) {
                    int cases = REQUIRED_CORNMEAL - currentCornmeal;
                    if (REQUIRED_CORNMEAL - currentCornmeal != 0) {
                        ;
                    }
                    orders.append(cases + " cornmeal (GFS) \n");
                }
                if (currentPineapple < REQUIRED_PINEAPPLE) {
                    int cases = REQUIRED_PINEAPPLE - currentPineapple;
                    if (REQUIRED_PINEAPPLE - currentPineapple != 0) {
                        ;
                    }
                    orders.append(cases + " pineapple (GFS) \n");
                }
                if (currentPrep < REQUIRED_PREP) {
                    int cases = (REQUIRED_PREP - currentPrep) / PREP_PER_CASE;
                    if ((REQUIRED_PREP - currentPrep) % PREP_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + "  prep containers (GFS) \n");
                }
                if (currentLids < REQUIRED_LIDS) {
                    int cases = (REQUIRED_LIDS - currentLids) / LIDS_PER_CASE;
                    if ((REQUIRED_LIDS - currentLids) % LIDS_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + "  prep lids (GFS) \n");
                }
                if (currentSanitabs < REQUIRED_SANITABS) {
                    int cases = REQUIRED_SANITABS - currentSanitabs;
                    if (REQUIRED_SANITABS - currentSanitabs != 0) {
                        ;
                    }
                    orders.append(cases + " Sani Tab Jugs (GFS) \n");
                }
                if (currentSeasoning < REQUIRED_SEASONING) {
                    int cases = REQUIRED_SEASONING - currentSeasoning;
                    if (REQUIRED_SEASONING - currentSeasoning != 0) {
                        ;
                    }
                    orders.append(cases + " Italian Seasoning Jugs (GFS) \n");
                }


                Button copyButton = findViewById(R.id.copy_button);
                EditText editText = findViewById(R.id.order_text_view);
                copyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // code to copy text from EditText goes here
                        String text = editText.getText().toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(text);

                    }
                });


                // Display list of items to order
                if (orders.length() > 0) {
                    orders.setLength(orders.length() - 2);
                    mOrderTextView.setText("Delco Order: \n" + orders.toString());
                    // Connect to MySQL and submit order
                    submitOrder(orders.toString());
                } else {
                    mOrderTextView.setText("No items to order.");
                }


            }


        });
    }


    private void submitOrder(final String orders) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://inv.thealleypub.com/androidinv.php";
        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String currentDate = sdf.format(new Date());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response
                        mOrderTextView.setText("Order submitted: \n" + orders);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                mOrderTextView.setText("Error submitting order: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("orderlist", orders.toString());
                params.put("date", currentDate);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
