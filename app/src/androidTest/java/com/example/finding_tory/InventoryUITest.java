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

    public void register(){
        onView(withId(R.id.button_register)).perform(click());
        onView(withId(R.id.edit_text_username)).perform(typeText("TestUser"));
        onView(withId(R.id.edit_text_name)).perform(typeText("TestUser"));
        onView(withId(R.id.edit_text_password)).perform(typeText("password123"));
        onView(withId(R.id.edit_text_confirm_password)).perform(typeText("password123"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.button_register)).perform(click());
    }

    public void createInventory() {
        onView(withId(R.id.add_inventory_button)).perform(click());
        onView(withId(R.id.inventoryNameText)).perform(ViewActions.typeText("Home"));
        onView(withId(R.id.submit_button)).perform(click());
    }

    private void testDeleteInventory() {
        onData(anything())
                .inAdapterView(withId(R.id.ledger_listview))
                .atPosition(0)
                .perform(longClick());
        onView(withId(R.id.delete_button)).perform(click());
        onView(withId(R.id.btnDelete)).perform(click());

    }

    public void testAddItem(String description, String make, String model, String price, String tags) {
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText(description));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText(make));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText(model));
        onView(withId(R.id.date_edittext)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2023, 7, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText(price));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("SN3892"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Bought online"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText(tags), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withText(description)).check(matches(isDisplayed()));
    }

    public void testDeleteItem(String item) {
        onView(withText(item)).check(matches(isDisplayed()));
        onView(withText(item)).perform(click());
        onView(withId(R.id.delete_button)).perform(click());
        onView(withId(R.id.btnDelete)).perform(click());
        onView(withText(item)).check(doesNotExist());
    }

    @Test
    public void loginAndLogout() {
        onView(withId(R.id.edit_text_username)).perform(ViewActions.typeText("abc"));
        onView(withId(R.id.edit_text_password)).perform(ViewActions.typeText("123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_login)).perform(click());
        awaitDB(2000);
        Espresso.pressBack();
        onView(withId(R.id.imageView2)).perform(click());
    }

    @Test
    public void mainTest(){
        register();
        awaitDB(2000);
        createInventory();
        awaitDB(1000);
        onData(anything())
                .inAdapterView(withId(R.id.ledger_listview))
                .atPosition(0)
                .perform(click());
        awaitDB(2000);
        onView(withId(R.id.add_delete_item_button)).perform(click());
        testAddItem("Sony PS5", "Sony", "PS5", "2000", "Electronics Consoles");
        onView(withText("Sony PS5")).check(matches(isDisplayed()));
        awaitDB(1000);
        onView(withId(R.id.add_delete_item_button)).perform(click());
        testAddItem("Ninja Blender AF100", "Ninja", "AF100", "100", "Kitchen");
        onView(withText("Ninja Blender AF100")).check(matches(isDisplayed()));
        Espresso.pressBack();
        awaitDB(1000);
        testDeleteInventory();
        FirestoreDB.getUsersRef().document("TestUser").delete();
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

}
