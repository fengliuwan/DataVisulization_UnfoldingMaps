package maps;


import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}
	
	
	// pg is the graphics object on which you call the graphics
	// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
	// x and y are the center of the object to draw. 
	// They will be used to calculate the coordinates to pass
	// into any shape drawing methods.  
	// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
	// whose upper left corner is at position x, y
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		//System.out.println("Drawing a city");
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		
		
		if(clicked) {
			List<Marker> qMarkers = EarthquakeCityMap.getQuakeMarkers();
			ArrayList<EarthquakeMarker> qMInRange = new ArrayList<EarthquakeMarker>();
			float totalMag = 0;
			String mostRecTitle = "N/A";

			for(Marker m : qMarkers) {
				double tCircle = ((EarthquakeMarker)m).threatCircle();
				double dist = this.getDistanceTo(m.getLocation());
				if(tCircle > dist) {
					qMInRange.add((EarthquakeMarker)m);
				}
			}
			
			//System.out.println(qMInRange);
			for(EarthquakeMarker em : qMInRange) {
				totalMag += Float.parseFloat(em.getProperty("magnitude").toString());
			}
			int count = qMInRange.size();
			float aveMag = (count == 0 ) ? 0 : totalMag/count;
			
			for(EarthquakeMarker e : qMInRange) {
				if(e.getProperty("age").toString().equals("Past Hour")) {
					mostRecTitle = e.getProperty("title").toString();
					break;
				} else if (e.getProperty("age").toString().equals("Past Day")) {
					mostRecTitle = e.getProperty("title").toString();
					break;
				} else if (e.getProperty("age").toString().equals("Past Week")) {
					mostRecTitle = e.getProperty("title").toString();
					break;
				}
			}
			
			
			pg.fill(220);
			pg.rect(x + TRI_SIZE, y + TRI_SIZE, 460, 40);
			pg.fill(0);
			pg.textSize(12);
			String toDisplay1 = "Number of nearby earthquakes: " + count;
			String toDisplay2 = "Average magnitude: " + aveMag;
			String toDisplay3 =	"Most recent earthquake is : " + mostRecTitle;
			pg.text(toDisplay1, x + TRI_SIZE, y + TRI_SIZE + 12);
			pg.text(toDisplay2, x + TRI_SIZE, y + TRI_SIZE + 24);
			pg.text(toDisplay3, x + TRI_SIZE, y + TRI_SIZE + 36);	
		}
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCity() + " " + getCountry() + " ";
		String pop = "Pop: " + getPopulation() + " Million";
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(pop)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(pop, x+3, y - TRI_SIZE -18);
		
		pg.popStyle();
	}
	
	private String getCity()
	{
		return getStringProperty("name");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
