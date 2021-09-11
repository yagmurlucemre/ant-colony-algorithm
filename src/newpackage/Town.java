/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;


public class Town{
	
	public int id;
	public double latitude;
	public double longitude;

	//constructor
	public Town(int id, double latitude, double longitude){
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	//return the distance between two latitude/longitude points im kilometres - uses Haversine
	public double distanceTo(Town to){

		if(this == to){
			return 0;//same town
		}

		//radius of the earth = 6371km
		double x1 = latitude;
		double y1 = longitude;
		double x2 = to.latitude;
		double y2 = to.longitude;
                final double xLerFarki = x2 - x1;
               final double yLerFarki = y2 - y1;
        return (Math.abs((Math.sqrt((xLerFarki * xLerFarki) + (yLerFarki * yLerFarki)))) + 1);
		
	}

	public Town copy(){
		Town toReturn = new Town(id, latitude, longitude);
		return toReturn;
	}

}