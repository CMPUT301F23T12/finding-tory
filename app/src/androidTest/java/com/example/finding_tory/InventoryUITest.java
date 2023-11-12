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


import static org.hamcrest.CoreMatchers.anything;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryUITest {
    @Rule
    public ActivityScenarioRule<LedgerViewActivity> activityRule = new ActivityScenarioRule<>(LedgerViewActivity.class);

    @Test
    public void testAddAndDeleteItem() {
        navigateToInventory();
        onView(withId(R.id.add_delete_item_button)).perform(click());

        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Apple Macbook Pro 2022"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Macbook"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("Pro 2022"));
        onView(withId(R.id.date_edittext)).perform(ViewActions.typeText("2023-01-28"));
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("2000"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("SN3892"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Bought online"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Living-Room"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withText("Apple Macbook Pro 2022")).check(matches(isDisplayed()));

        navigateToFirstItem();
        onView(withId(R.id.button_delete_item)).perform(click());
        onView(withId(R.id.btnDelete)).perform(click());
        onView(withText("Apple Macbook Pro 2022")).check(doesNotExist());
    }

    @Test
    public void testCancelAddItem() {
        navigateToInventory();
        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.cancel_button)).perform(scrollTo());
        onView(withId(R.id.cancel_button)).perform(click());
    }

    @Test
    public void testGoBackToLedger() {
        navigateToInventory();
        navigateToFirstItem();
        onView(withId(R.id.button_close_item_view)).perform(click());
        pressBack();
        onView(withText("Home Inventory")).check(matches(isDisplayed()));
    }

    @Test
    public void testSortItems() {
        navigateToInventory();
        onView(withId(R.id.sort_cancel_button)).perform(click());
        onView(withId(R.id.radio_Value)).perform(click());
        onView(withId(R.id.ascending_descending)).perform(click());
        onView(withId(R.id.btnSort)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .perform(click());

        // Verify first item clicked is now TV
        onView(withText("Sony TV")).check(matches(isDisplayed()));
    }

    @Test
    public void testViewItem() {
        // Click on the first inventory in the ledger list
        // Make sure testAddItem() is ran first
        navigateToInventory();
        navigateToFirstItem();
        onView(withText("Blender")).check(matches(isDisplayed()));
    }

    @Test
    public void testbulkDeleteItem() {
        navigateToInventory();

        // Long click first item in the inventory
        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .perform(ViewActions.longClick());

        // Bulk delete Blender and Dining Table
        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .onChildView(withId(R.id.item_checkbox))
                .perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(2)
                .onChildView(withId(R.id.item_checkbox))
                .perform(click());

        onView(withId(R.id.filter_tag_button)).perform(click());

        onView(withText("Blender")).check(doesNotExist());
        onView(withText("Dining Table")).check(doesNotExist());
        onView(withId(R.id.btnDelete)).perform(click());

        // Reset the app to it's original state since the DB is affected
        resetAppState();
    }

    public void navigateToInventory() {
        onData(anything())
                .inAdapterView(withId(R.id.ledger_listview))
                .atPosition(0)
                .perform(click());
    }

    public void navigateToFirstItem() {
        onData(anything())
                .inAdapterView(withId(R.id.inventory_listview))
                .atPosition(0)
                .perform(click());
    }

    public void resetAppState() {
        // Add all the stuff we deleted since DB is affected permanently
        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Blender"));
        onView(withId(R.id.make_edittext)).perform(ViewActions.typeText("Ninja"));
        onView(withId(R.id.model_edittext)).perform(ViewActions.typeText("B600"));
        onView(withId(R.id.date_edittext)).perform(ViewActions.typeText("2023-08-01"));
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.serial_number_edittext)).perform(ViewActions.typeText("GN54E"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Bought online"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_edittext)).perform(ViewActions.typeText("Kitchen Home"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_tags_button)).perform(click());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.add_delete_item_button)).perform(click());
        onView(withId(R.id.description_edittext)).perform(ViewActions.typeText("Dining Table"));
        onView(withId(R.id.date_edittext)).perform(ViewActions.typeText("2019-02-18"));
        onView(withId(R.id.amount_edittext)).perform(ViewActions.typeText("800"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.comment_edittext)).perform(ViewActions.typeText("Oak Wood Dining Table"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(scrollTo());
        onView(withId(R.id.add_button)).perform(click());
    }
}
