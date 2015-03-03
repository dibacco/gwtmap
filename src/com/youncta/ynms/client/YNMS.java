package com.youncta.ynms.client;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener.FeatureSelectedEvent;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.Style;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
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


	public static void openNewWindow(String name, String url) {
	    com.google.gwt.user.client.Window.open(url, name.replace(" ", "_"),
	           "menubar=no," + 
	           "location=false," + 
	           "resizable=yes," + 
	           "scrollbars=yes," + 
	           "status=no," + 
	           "dependent=true");
	}

	
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
		
        final Vector vectorLayer = new Vector("Vectorlayer");

        Style pointStyle = new Style();
        pointStyle.setExternalGraphic("ynms/img/y.svg");
        pointStyle.setGraphicSize(32, 32);
        pointStyle.setFillOpacity(1.0);

		LonLat lonLat1 = new LonLat(73.36, 54.98);
		lonLat1.transform("EPSG:4326", mapWidget.getMap().getProjection()); //transform lonlat (provided in EPSG:4326) to OSM coordinate system (the map projection)
        Point point1 = new Point(lonLat1.lon(), lonLat1.lat());
        
		LonLat lonLat2 = new LonLat(73.37, 55.00);
		lonLat2.transform("EPSG:4326", mapWidget.getMap().getProjection()); //transform lonlat (provided in EPSG:4326) to OSM coordinate system (the map projection)
        Point point2 = new Point(lonLat2.lon(), lonLat2.lat());

		LonLat lonLat3 = new LonLat(73.30, 54.96);
		lonLat3.transform("EPSG:4326", mapWidget.getMap().getProjection()); //transform lonlat (provided in EPSG:4326) to OSM coordinate system (the map projection)
        Point point3 = new Point(lonLat3.lon(), lonLat3.lat());

		LonLat lonLat4 = new LonLat(73.28, 54.94);
		lonLat4.transform("EPSG:4326", mapWidget.getMap().getProjection()); //transform lonlat (provided in EPSG:4326) to OSM coordinate system (the map projection)
        Point point4 = new Point(lonLat4.lon(), lonLat4.lat());

        VectorFeature pointFeature1 = new VectorFeature(point1, pointStyle);
        VectorFeature pointFeature2 = new VectorFeature(point2, pointStyle);
        VectorFeature pointFeature3 = new VectorFeature(point3, pointStyle);
        VectorFeature pointFeature4 = new VectorFeature(point4, pointStyle);

        vectorLayer.addFeature(pointFeature1);
        vectorLayer.addFeature(pointFeature2);
        vectorLayer.addFeature(pointFeature3);
        vectorLayer.addFeature(pointFeature4);
        
        mapWidget.getMap().addLayer(vectorLayer);

        final SelectFeature selectFeature = new SelectFeature(vectorLayer);
        selectFeature.setAutoActivate(true);
        mapWidget.getMap().addControl(selectFeature);

        vectorLayer.addVectorFeatureSelectedListener(new VectorFeatureSelectedListener() {
            public void onFeatureSelected(FeatureSelectedEvent eventObject) {
                selectFeature.unSelect(eventObject.getVectorFeature());
                openNewWindow("Y-Packet", "http://52.10.185.208");
            }
        });

		RootLayoutPanel.get().add(mapWidget);
	}
}
