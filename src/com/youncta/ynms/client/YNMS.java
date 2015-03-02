package com.youncta.ynms.client;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.layer.OSM;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class YNMS implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";



	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		MapOptions defaultMapOptions = new MapOptions();
		MapWidget mapWidget = new MapWidget("100%", "100%", defaultMapOptions);

		OSM osmMapnik = OSM.Mapnik("Mapnik");

		osmMapnik.setIsBaseLayer(true);

		mapWidget.getMap().addLayer(osmMapnik);
		
		LonLat lonLat = new LonLat(73.36, 54.98);
		lonLat.transform("EPSG:4326", mapWidget.getMap().getProjection()); //transform lonlat (provided in EPSG:4326) to OSM coordinate system (the map projection)
		mapWidget.getMap().setCenter(lonLat, 12);
		
		RootLayoutPanel.get().add(mapWidget);
	}
}
