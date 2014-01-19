package com.igates.example.locationbasedservicesample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igates.example.locationbasedservicesample.R;
import com.igates.example.locationbasedservicesample.data.RouteLocation;
import com.igates.example.locationbasedservicesample.database.RouteDatabase;

public class AllRoutesActivity extends Activity
{

	RouteDatabase sqlHelper;
	CursorAdapter myAdapter;
	ListView allRouteslistView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Model
		sqlHelper = new RouteDatabase(this);

		// View
		setContentView(R.layout.activity_all_routes);
		allRouteslistView = (ListView) findViewById(R.id.allRouteslistView);
		Button createNewRouteButton = (Button) findViewById(R.id.addNewRoute);

		// Controller
		Cursor cursorAllRoutesNames = sqlHelper.getAllRoutesNames();
		myAdapter = new CustomAllRoutesCursorAdapter(getApplicationContext(), cursorAllRoutesNames);
		allRouteslistView.setAdapter(myAdapter);

		allRouteslistView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
			{
				Intent goToRouteLocations = new Intent(getApplicationContext(), RoutesListActivity.class);
				goToRouteLocations.putExtra("routeName", (String) v.getTag());
				startActivity(goToRouteLocations);

			}
		});

		registerForContextMenu(allRouteslistView);

		createNewRouteButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditText newRouteName = (EditText) AllRoutesActivity.this.findViewById(R.id.newRouteName);
				if(newRouteName.getText().toString() != null)
				{
					Intent createNewRoute = new Intent(getBaseContext(), MainActivity.class);
					createNewRoute.putExtra("routeName", newRouteName.getText().toString());
					startActivity(createNewRoute);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "New route name cannot be empty", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onResume()
	{
		((EditText) findViewById(R.id.newRouteName)).setText("");
		Cursor cursorAllRoutesNames = sqlHelper.getAllRoutesNames();
		myAdapter.changeCursor(cursorAllRoutesNames);
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		sqlHelper.close();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_routes, menu);
		return true;
	}

	public class CustomAllRoutesCursorAdapter extends CursorAdapter
	{
		LayoutInflater mLayoutInflater;

		public CustomAllRoutesCursorAdapter(Context context, Cursor c)
		{
			super(context, c, 0);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, final Context context, Cursor cursor)
		{

			final String routeName = cursor.getString(cursor.getColumnIndex(RouteDatabase.KEY_ROUTE_NAME));

			TextView routeNameTextView = (TextView) view.findViewById(R.id.routeName);
			view.setTag(routeName);
			routeNameTextView.setText(routeName);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{

			View view = mLayoutInflater.inflate(R.layout.route, parent, false);
			return view;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.routes_list_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		RouteLocation routeLocation = sqlHelper.getRouteLocation(info.id);
		String routeName = routeLocation.getRouteName();

		switch (item.getItemId())
		{
			case R.id.goToLocations:
				goToLocations(routeName);
				return true;
			case R.id.deleteRoute:
				deleteRoute(routeName);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	private void deleteRoute(String routeName)
	{
		sqlHelper.removeAllRouteLocations(routeName);

		Cursor cursorAllRoutesNames = sqlHelper.getAllRoutesNames();
		myAdapter.changeCursor(cursorAllRoutesNames);
	}

	private void goToLocations(String routeName)
	{
		Intent goToRouteLocations = new Intent(getApplicationContext(), RoutesListActivity.class);
		goToRouteLocations.putExtra("routeName", routeName);
		startActivity(goToRouteLocations);

	}
}
