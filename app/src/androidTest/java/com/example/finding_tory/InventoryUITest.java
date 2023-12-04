package com.example.finding_tory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static org.hamcrest.Matchers.allOf;

import static org.hamcrest.CoreMatchers.anything;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import static androidx.test.espresso.action.ViewActions.*;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryUITest {
    @Rule
    public ActivityScenarioRule<LedgerViewActivity> activityRule = new ActivityScenarioRule<>(LedgerViewActivity.class);

    private void awaitDB(int timer) {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void register(){
        onView(withId(R.id.button_register)).perform(click());
        onView(withId(R.id.edit_text_username)).perform(typeText("TestUser"));
        onView(withId(R.id.edit_text_name)).perform(typeText("TestUser"));
        onView(withId(R.id.edit_text_password)).perform(typeText("password123"));
        onView(withId(R.id.edit_text_confirm_password)).perform(typeText("password123"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void login() {
        onView(withId(R.id.edit_text_username)).perform(ViewActions.typeText("abc"));
        onView(withId(R.id.edit_text_password)).perform(ViewActions.typeText("123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test
    public void createInventory() {
        onView((withId(R.id.add_inventory_button))).perform(click());
        onView(withId(R.id.inventoryNameText)).perform(ViewActions.typeText("Home"));
        onView(withId(R.id.submit_button)).perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.ledger_listview))
                .atPosition(0)
                .perform(click());
    }

    @Test
    public void testAddItem() {
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Apple Macbook Pro 2022"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Macbook"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("Pro 2022"));
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("2000"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("SN3892"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Bought online"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Living-Room"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withText("Apple Macbook Pro 2022")).check(matches(isDisplayed()));
    }

    @Test
    public void mainTest(){
        register();
        awaitDB(2000);
        createInventory();
        awaitDB(2000);
        onData(anything())
                .inAdapterView(withId(R.id.ledger_listview))
                .atPosition(0)
                .perform(click());
        awaitDB(2000);
        onView(withId(R.id.add_delete_item_button)).perform(click());
        awaitDB(1000);
        testAddItem();
        FirestoreDB.getUsersRef().document("TestUser").delete();
    }

    @Test
    public void navigateToInventory() {
        register();
        awaitDB(2000);

        onView(withId(R.id.inventoryNameText)).perform(ViewActions.typeText("Home"));
        onView(withId(R.id.submit_button)).perform(click());

        awaitDB(3000);

        onView(withId(R.id.search_inventory_edittext)).perform(typeText("Home"), ViewActions.closeSoftKeyboard());

        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .perform(click());
    }

    private void navigateToMainScreen() {
        login();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.ledger_listview)).check(matches(isDisplayed()));
    }


    private void deleteInventory(String inventoryName) {
        navigateToMainScreen();

        onView(withId(R.id.search_inventory_edittext)).perform(typeText(inventoryName), ViewActions.closeSoftKeyboard());

        try {
            Thread.sleep(2000); // This is not a best practice. Consider using Idling Resources.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything())
                .inAdapterView(withId(R.id.ledger_listview))
                .atPosition(0)
                .onChildView(withText(inventoryName))
                .perform(longClick());

        onView(withId(R.id.delete_button)).perform(click());
    }

    public void navigateToFirstItem() {
        awaitDB(2000);

        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ViewAction setDate(final int year, final int monthOfYear, final int dayOfMonth) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(DatePicker.class);
            }

            @Override
            public String getDescription() {
                return "Set the date in the DatePickerDialog to the specified values";
            }

            @Override
            public void perform(UiController uiController, View view) {
                final DatePicker datePicker = (DatePicker) view;
                datePicker.updateDate(year, monthOfYear, dayOfMonth);
            }
        };
    }

    @Test
    public void testCancelAddItem() {
        try {
            navigateToInventory();
        }
        catch (Exception e) {
            
        }
        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.cancel_button)).perform(scrollTo());
        onView(withId(R.id.cancel_button)).perform(click());
    }

    @Test
    public void testGoBackToLedger() {
        try {
            navigateToInventory();
        }
        catch (Exception e) {
            
        }
        navigateToFirstItem();
        onView(withId(R.id.button_close_item_view)).perform(click());
        pressBack();
        onView(withText("Home")).check(matches(isDisplayed()));
    }

    @Test
    public void testSortItems() {
        try {
            navigateToInventory();
        }
        catch (Exception e) {
            
        }
        onView(withId(R.id.sort_cancel_button)).perform(click());
        onView(withId(R.id.radio_Value)).perform(click());
        onView(withId(R.id.radio_descending)).perform(click());
        onView(withId(R.id.btnSort)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .perform(click());

        // Verify first item clicked is now Dining Table
        onView(withText("Dining Table")).check(matches(isDisplayed()));
    }

    @Test
    public void testViewItem() {
        // Click on the first inventory in the ledger list
        // Make sure testAddItem() is ran first
        try {
            navigateToInventory();
        }
        catch (Exception e) {
            
        }
        onView(withText("Blender")).check(matches(isDisplayed()));
    }


    public void resetAppState() {
        navigateToInventory();

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Blender"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Ninja"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("B600"));
        // for Interaction with DatePickerDialog
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("GN54E"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Bought online"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Kitchen Home"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Dining Table"));
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("800"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Oak Wood Dining Table"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Sony PS5"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Sony"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("PS5"));
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("500"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("JKA34"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Brand New"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Living-Room Electronics Home"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());
    }

    @Test
    public void addTestData2() throws InterruptedException {
        Thread.sleep(2000);
        navigateToInventory();

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Blender"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Ninja"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("B600"));
        // for Interaction with DatePickerDialog
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("GN54E"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Bought online"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Kitchen Home"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Dining Table"));
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("800"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Oak Wood Dining Table"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Sony PS5"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Sony"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("PS5"));
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("500"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("JKA34"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Brand New"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Living-Room Electronics Home"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());
    }
}
