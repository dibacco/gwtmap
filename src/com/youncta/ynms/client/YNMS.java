package com.youncta.ynms.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.RenderIntent;
import org.gwtopenmaps.openlayers.client.control.Scale;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.event.FeatureHighlightedListener;
import org.gwtopenmaps.openlayers.client.event.FeatureUnhighlightedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener.FeatureSelectedEvent;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.popup.FramedCloud;
import org.gwtopenmaps.openlayers.client.popup.Popup;
import org.gwtopenmaps.openlayers.client.Style;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

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

	  // The list of data to display.
	  private static List<AlarmEvent> EVENTS = Arrays.asList(
			  new AlarmEvent("Equipment 1", "192.168.0.25", "Dem failure"), 
	      	  new AlarmEvent("Equipment 2", "192.168.0.29", "Rx failure"), 
	      	  new AlarmEvent("Equipment 3", "192.168.0.30", "Rx failure"), 
	      	  new AlarmEvent("Equipment 4", "192.168.0.34", "Rx failure"), 
	      	  new AlarmEvent("Equipment 3", "192.168.0.35", "Rx failure"), 
	      	  new AlarmEvent("Equipment 4", "192.168.0.78", "Rx failure"), 
	      	  new AlarmEvent("Equipment 1", "192.168.0.90", "Rx failure"), 
	      	  new AlarmEvent("Equipment 1", "192.168.0.30", "Tx failure"));

	  private static List<Equipment> EQUIPMENT = Arrays.asList(
			  new Equipment("Node 1", "192.168.0.25", new LonLat(84.96, 56.45)), 
	      	  new Equipment("Node 2", "192.168.0.29", new LonLat(84.98, 56.47)), 
	      	  new Equipment("Node 3", "192.168.0.30", new LonLat(84.93, 56.49)), 
	      	  new Equipment("Node 4", "192.168.0.34", new LonLat(84.97, 56.52)),
			  new Equipment("Node 5", "192.168.0.27", new LonLat(9.18, 45.46)), 
	      	  new Equipment("Node 6", "192.168.0.41", new LonLat(9.16, 45.48)), 
	      	  new Equipment("Node 7", "192.168.0.33", new LonLat(9.19, 45.49)), 
	      	  new Equipment("Node 8", "192.168.0.89", new LonLat(9.17, 45.45))	      	  
			  );
	  

	public static void openNewWindow(String name, String url) {
	    com.google.gwt.user.client.Window.open(url, name.replace(" ", "_"),
	           "menubar=no," + 
	           "location=false," + 
	           "resizable=yes," + 
	           "scrollbars=yes," + 
	           "status=no," + 
	           "dependent=true");
	}
	
	static Label labelMessage = null;
	MapWidget mapWidget = null;
	Map map = null;
	
	public  Tree buildTreeMenu() {
	      // Create a root tree item as department
	      TreeItem region1 = new TreeItem();
	      region1.setText("Томск");
	      

	      TreeItem node;
	      //create other tree items as department names
	      node = new TreeItem();
	      node.setText("Node 1");
	      region1.addItem(node);

	      node = new TreeItem();
	      node.setText("Node 2");
	      region1.addItem(node);

	      node = new TreeItem();
	      node.setText("Node 3");
	      region1.addItem(node);

	      node = new TreeItem();
	      node.setText("Node 4");
	      region1.addItem(node);


	      TreeItem region2 = new TreeItem();
	      region2.setText("Milan");
	      
	      node = new TreeItem();
	      node.setText("Node 5");
	      region2.addItem(node);

	      node = new TreeItem();
	      node.setText("Node 6");
	      region2.addItem(node);

	      node = new TreeItem();
	      node.setText("Node 7");
	      region2.addItem(node);

	      node = new TreeItem();
	      node.setText("Node 8");
	      region2.addItem(node);


	      //create the tree
	      Tree tree = new Tree();

	      //add root item to the tree
	      tree.addItem(region1);	   
	      tree.addItem(region2);	   

	      tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
	         @Override
	         public void onSelection(SelectionEvent<TreeItem> event) {
	            String node = event.getSelectedItem().getText();
            	LonLat lonLat = null;

            	for (int i = 0; i < EQUIPMENT.size(); i++) {
            		if (node.equals(EQUIPMENT.get(i).name)) {
            			lonLat = EQUIPMENT.get(i).lonLat;
            			map.panTo(lonLat);
    	            	//map.setCenter(lonLat, 12);	
            		}
            	}
            	

	         }
	      });
	      
	      return tree;

	}
	
	public  CellTable<AlarmEvent> buildEventLog() {
	    // Create a CellTable.
	    final CellTable<AlarmEvent> table = new CellTable<AlarmEvent>();

	    // Create name column.
	    TextColumn<AlarmEvent> nameColumn = new TextColumn<AlarmEvent>() {
	      @Override
	      public String getValue(AlarmEvent equipment) {
	        return equipment.name;
	      }
	    };

	    // Make the name column sortable.
	    nameColumn.setSortable(true);

	    // Create address column.
	    TextColumn<AlarmEvent> addressColumn = new TextColumn<AlarmEvent>() {
	      @Override
	      public String getValue(AlarmEvent equipment) {
	        return equipment.address;
	      }
	    };


	    // Create address column.
	    TextColumn<AlarmEvent> alarmColumn = new TextColumn<AlarmEvent>() {
	      @Override
	      public String getValue(AlarmEvent equipment) {
	        return equipment.alarm;
	      }
	    };

	    // Add the columns.
	    table.addColumn(nameColumn, "Name");
	    table.addColumn(addressColumn, "Address");
	    table.addColumn(alarmColumn, "Alarm");

	    table.setWidth("100%");
	    
	    // Set the total row count. You might send an RPC request to determine the
	    // total row count.
	    table.setRowCount(EVENTS.size(), true);

	    // Set the range to display. In this case, our visible range is smaller than
	    // the data set.
	    table.setVisibleRange(0, 6);

	    // Create a data provider.
	    AsyncDataProvider<AlarmEvent> dataProvider = new AsyncDataProvider<AlarmEvent>() {
	      @Override
	      protected void onRangeChanged(HasData<AlarmEvent> display) {
	        final Range range = display.getVisibleRange();

	        // Get the ColumnSortInfo from the table.
	        final ColumnSortList sortList = table.getColumnSortList();

	        // This timer is here to illustrate the asynchronous nature of this data
	        // provider. In practice, you would use an asynchronous RPC call to
	        // request data in the specified range.
	        new Timer() {
	          @Override
	          public void run() {
	            int start = range.getStart();
	            int end = start + range.getLength();
	            // This sorting code is here so the example works. In practice, you
	            // would sort on the server.

	            List<AlarmEvent> dataInRange = EVENTS.subList(start, end);

	            // Push the data back into the list.
	            table.setRowData(start, dataInRange);
	          }
	        }.schedule(2000);
	      }
	    };

	    // Connect the list to the data provider.
	    dataProvider.addDataDisplay(table);

	    // Add a ColumnSortEvent.AsyncHandler to connect sorting to the
	    // AsyncDataPRrovider.
	    AsyncHandler columnSortHandler = new AsyncHandler(table);
	    table.addColumnSortHandler(columnSortHandler);

	    // We know that the data is sorted alphabetically by default.
	    table.getColumnSortList().push(nameColumn);

	    return table;
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

	    labelMessage = new Label();
	    labelMessage.setWidth("300");

		MapOptions defaultMapOptions = new MapOptions();
		mapWidget = new MapWidget("100%", "100%", defaultMapOptions);
		
		OSM osmMapnik = OSM.Mapnik("Mapnik");

		osmMapnik.setIsBaseLayer(true);

		mapWidget.getMap().addLayer(osmMapnik);
		map = mapWidget.getMap();
		
        map.addControl(new ScaleLine()); //Display the scaleline
        map.addControl(new Scale());

		LonLat lonLat = new LonLat(84.96, 56.48);
		lonLat.transform("EPSG:4326", mapWidget.getMap().getProjection()); //transform lonlat (provided in EPSG:4326) to OSM coordinate system (the map projection)
		mapWidget.getMap().setCenter(lonLat, 12);
		
        final Vector vectorLayer = new Vector("Vectorlayer");

        Style pointStyle = new Style();
        pointStyle.setExternalGraphic("ynms/img/y.svg");
        pointStyle.setGraphicSize(32, 32);
        pointStyle.setFillOpacity(1.0);

        Style hoverStyle = new Style();
        hoverStyle.setFillColor("blue");
        hoverStyle.setStrokeColor("pink");
        hoverStyle.setStrokeWidth(2);
        hoverStyle.setFillOpacity(0.9);
        hoverStyle.setPointRadius(30);
        
        for (int i = 0; i < EQUIPMENT.size(); i++) {
        	LonLat ll =  EQUIPMENT.get(i).lonLat;
        	ll.transform("EPSG:4326", mapWidget.getMap().getProjection());
        	VectorFeature  pointFeature = new VectorFeature(new Point(ll.lon(), ll.lat()), pointStyle);
        	pointFeature.setFeatureId(EQUIPMENT.get(i).name);
            
            final Popup popup = new FramedCloud("id1",
                    ll, null,
                    "<h3>"+pointFeature.getFeatureId()+"</h3><br>No alarm", null, false);
            popup.setPanMapIfOutOfView(true); // this set the popup in a strategic
            // way, and pans the map if needed.
            popup.setAutoSize(true);
            pointFeature.setPopup(popup);
            
            vectorLayer.addFeature(pointFeature);
        }
        
        mapWidget.getMap().addLayer(vectorLayer);

        final SelectFeature selectFeature = new SelectFeature(vectorLayer);
        selectFeature.setAutoActivate(true);
 
        // SelectFeature control to capture hover on the vectors
        SelectFeatureOptions selectFeatureHoverOptions = new SelectFeatureOptions();
        // use the tempory style to be defined in the StyleMap     
        selectFeatureHoverOptions.setRenderIntent(RenderIntent.TEMPORARY);
        selectFeatureHoverOptions.setHighlightOnly(true);
        selectFeatureHoverOptions.setHover();
        SelectFeature selectHoverFeature = new SelectFeature(vectorLayer,  selectFeatureHoverOptions);
        selectHoverFeature.setClickOut(false);
        selectHoverFeature.setAutoActivate(true);
 
        mapWidget.getMap().addControl(selectHoverFeature);
        mapWidget.getMap().addControl(selectFeature);
        
        vectorLayer.addVectorFeatureSelectedListener(new VectorFeatureSelectedListener() {
            public void onFeatureSelected(FeatureSelectedEvent eventObject) {
                selectFeature.unSelect(eventObject.getVectorFeature());
                String url = GWT.getHostPageBaseURL();
                url = url.substring(0, url.lastIndexOf(':')+1)+"9090";
                openNewWindow("Y-Packet", url);
            }
        });
        
        // capture hover by adding a listener to the control, and display the
        // popup
        selectHoverFeature.addFeatureHighlightedListener(new FeatureHighlightedListener() {
 
                    public void onFeatureHighlighted(VectorFeature vectorFeature) {
                        map.addPopup(vectorFeature.getPopup());
                    }
 
                });
 
        // capture unhover, and remove popup
        selectHoverFeature
                .addFeatureUnhighlightedListener(new FeatureUnhighlightedListener() {
 
                    public void onFeatureUnhighlighted(VectorFeature vectorFeature) {
                                map.removePopup(vectorFeature.getPopup());
                            }
 
                });
        
        CellTable<AlarmEvent> table = buildEventLog();
        
        Tree treeMenu = buildTreeMenu();
        
        
        SplitLayoutPanel p = new SplitLayoutPanel();
        TextBox ts = new TextBox();
        
        VerticalPanel westPanel = new VerticalPanel();
        labelMessage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        labelMessage.setWidth("100%");
        labelMessage.setText("Node List");

        westPanel.add(labelMessage);

        westPanel.add(treeMenu);
        p.addWest(westPanel, 256);
        
        ScrollPanel scrollPanel = new ScrollPanel(table);
        scrollPanel.setSize("100%", "100%");

        p.addSouth(scrollPanel, 128);
        p.add(mapWidget);

        
		RootLayoutPanel.get().add(p);
	}
}
