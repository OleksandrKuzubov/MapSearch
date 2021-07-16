package com.kutuzov.mapsearch

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.mapsearch.ui.MainActivity
import com.kutuzov.mapsearch.ui.favorites.FavoritesAdapter
import com.kutuzov.mapsearch.ui.main.MainFragmentDirections
import kotlinx.android.synthetic.main.favorit_item_layout.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesTest {

    private lateinit var navController: NavController
    private lateinit var activity: AppCompatActivity
    private lateinit var adapter: FavoritesAdapter

    /**
     * Use [ActivityScenario] to create and launch the activity under test. This is a
     * replacement for [androidx.test.rule.ActivityTestRule].
     */
    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        activityScenarioRule.scenario.onActivity {
            navController = it.findNavController(R.id.nav_host_fragment)
            activity = it
        }
        activity.runOnUiThread {
            navController.navigate(MainFragmentDirections.actionMapFragmentToFavorites())
        }
    }

    @Test
    fun test_view_holders() = runBlocking {
        delay(2000)

        adapter = activity.findViewById<RecyclerView>(R.id.recycler_view).adapter as FavoritesAdapter

        adapter.currentList.forEachIndexed { index: Int, address: FavoriteAddress ->
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<FavoritesAdapter.FavoriteItemViewHolder>(
                    index
                )
            )
            testPreview(index)

        }

        delay(2000)
    }

    private fun testPreview(index: Int) {
        Espresso.onView(withRecyclerView(R.id.recycler_view)?.atPosition(index))
            .check { view, noViewFoundException ->
                if (noViewFoundException != null) throw  noViewFoundException

                ViewMatchers.assertThat(view.address, ViewMatchers.isDisplayed())
                ViewMatchers.assertThat(view.post_code, ViewMatchers.isDisplayed())
                ViewMatchers.assertThat(view.country, ViewMatchers.isDisplayed())
            }
    }
}

fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher? {
    return RecyclerViewMatcher(recyclerViewId)
}