package com.example.utatracker;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class Represents a UTATraxXMLParser. It takes in the retrieved BufferedInputStream from the
 * NetworkActivity (AsyncTask).
 *
 */
public class UTATraxXMLParser {

    /**
     * Reads the BufferedInputStream from start to end and grabs the text of each xml tag
     * and creates a MonitoredVehicleByRoute object that has all vehicles given the requested
     * route.
     * @param in
     * @return List of MonitoredVehiclesByRoute
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<MonitoredVehicleByRoute> parse(BufferedInputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(in,null);

            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    /**
     * The readFeed method takes the setup XmlPullParser and does all the necessary parsing.
     * Goes line by line and grabs the information of desired xml tags.
     * Builds a List<MonitoredVehicleByRoute> for n number of MonitoredVehicles returned by the UTA API.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static List<MonitoredVehicleByRoute> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<MonitoredVehicleByRoute> vehiclesByRoute = new ArrayList();

        int eventType = parser.getEventType();
        MonitoredVehicleByRoute v = new MonitoredVehicleByRoute();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch(eventType){

                case XmlPullParser.START_TAG:
                    if(tagName.isEmpty()){
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("ResponseTimestamp")){
                        //ResponseTimeStamp will only be recorded once and found in the first
                        //MonitoredVehiclebyRoute at vehiclesByRoute[0]. All other objects will have null for these parameters
                        v.setResponseTimestamp(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("ValidUntil")){
                        //ValidUntil will only be recorded once and found in the first
                        //MonitoredVehiclebyRoute at vehiclesByRoute[0]. All other objects will have null for these parameters
                        v.setValidUntil(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("RecordedAtTime")){
                        //RecordedAtTime will only be recorded once and found in the first
                        //MonitoredVehiclebyRoute at vehiclesByRoute[0]. All other objects will have null for these parameters
                        v.setRecordedAtTime(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("LineRef")){
                        v.setLineRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("DirectionRef")){
                        v.setDirectionRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("DataFrameRef")){
                        v.setDataFrameRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("DatedVehicleJourneyRef")){
                        v.setDatedVehicleJourneyRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("PublishedLineName")){
                        v.setPublishedLineName(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("OriginRef")){
                        v.setOriginRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("DestinationRef")){
                        v.setDestinationRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("Monitored")){
                        v.setMonitored(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("Longitude")){
                        v.setLongitude(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("Latitude")){
                        v.setLatitude(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("ProgressRate")){
                        v.setProgressRate(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("CourseOfJourneyRef")){
                        v.setCourseOfJourneyRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("VehicleRef")){
                        v.setVehicleRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("StopPointRef")){
                        //If Monitored = False, these values will be null
                        //else if Monitored = True, these will have values.
                        v.setStopPointRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("VisitNumber")){
                        //If Monitored = False, these values will be null
                        //else if Monitored = True, these will have values.
                        v.setVisitNumber(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("VehicleAtStop")){
                        //If Monitored = False, these values will be null
                        //else if Monitored = True, these will have values.
                        v.setVehicleAtStop(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("LastGPSFix")){
                        v.setLastGPSFix(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("Scheduled")){
                        v.setScheduled(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("Bearing")){
                        v.setBearing(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("Speed")){
                        v.setSpeed(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("DestinationName")){
                        v.setDestinationName(parser.nextText());
                        eventType = parser.next();
                    } else{
                        eventType = parser.next();
                    }
                    break;
                case XmlPullParser.TEXT:
                    eventType = parser.next();
                    break;
                case XmlPullParser.END_TAG:
                    //Add MonitoredVehiclebyRoute to list and create a new MonitoredVehiclebyRoute
                    if(tagName.equalsIgnoreCase("MonitoredVehicleJourney")){
                        vehiclesByRoute.add(v);
                        v = new MonitoredVehicleByRoute();
                        eventType = parser.next();
                    }
                    else{
                        eventType = parser.next();
                    }

                    break;
                default:
                   eventType = parser.next();
                    break;
            }
        }
        return vehiclesByRoute;
    }

    static class MonitoredVehicleByRoute {
        //Represent a <tag> description from the returned XML.
        private String responseTimestamp;
        private String validUntil;
        private String recordedAtTime;
        private String dataFrameRef;
        private String datedVehicleJourneyRef;
        private String publishedLineName;
        private String originRef;
        private String destinationRef;
        private String lineRef;
        private String directionRef;
        private String monitored;
        private String longitude;
        private String latitude;
        private String progressRate;
        private String courseOfJourneyRef;
        private String vehicleRef;
        private String stopPointRef;
        private String visitNumber;
        private String vehicleAtStop;
        private String lastGPSFix;
        private String scheduled;
        private String bearing;
        private String speed;
        private String destinationName;

        /**
         * This class represents a MonitoredVehicleByRoute and stores all vehicles information
         * retrieved from an API by route call.
         * @param dataFrameRef
         * @param datedVehicleJourneyRef
         * @param publishedLineName
         * @param originRef
         * @param destinationRef
         * @param lineRef
         * @param directionRef
         * @param monitored
         * @param longitude
         * @param latitude
         * @param progressRate
         * @param courseOfJourneyRef
         * @param vehicleRef
         * @param stopPointRef
         * @param visitNumber
         * @param vehicleAtStop
         * @param lastGPSFix
         * @param scheduled
         * @param bearing
         * @param speed
         * @param destinationName
         * @param responseTimestamp
         * @param validUntil
         * @param recordedAtTime
         */
        private MonitoredVehicleByRoute(String dataFrameRef,String datedVehicleJourneyRef ,String publishedLineName, String originRef, String destinationRef,
                                        String lineRef, String directionRef,String monitored ,String longitude, String latitude,String progressRate, String courseOfJourneyRef,
                                        String vehicleRef,String stopPointRef ,String visitNumber,String vehicleAtStop,String lastGPSFix, String scheduled, String bearing, String speed, String destinationName, String responseTimestamp,
                                        String validUntil, String recordedAtTime ) {

            this.responseTimestamp = responseTimestamp;
            this.validUntil = validUntil;
            this.recordedAtTime = recordedAtTime;
            this.dataFrameRef = dataFrameRef;
            this.datedVehicleJourneyRef = datedVehicleJourneyRef;
            this.publishedLineName = publishedLineName;
            this.originRef = originRef;
            this.destinationRef = destinationRef;
            this.lineRef = lineRef;
            this.directionRef = directionRef;
            this.monitored = monitored;
            this.longitude = longitude;
            this.latitude = latitude;
            this.progressRate = progressRate;
            this.courseOfJourneyRef = courseOfJourneyRef;
            this.vehicleRef = vehicleRef;
            this.stopPointRef = stopPointRef;
            this.visitNumber = visitNumber;
            this.vehicleAtStop = vehicleAtStop;
            this.lastGPSFix = lastGPSFix;
            this.scheduled = scheduled;
            this.bearing = bearing;
            this.speed = speed;
            this.destinationName = destinationName;

        }
        //Constructor
        private MonitoredVehicleByRoute(){

        }
        //Getters and Setters
        private String getStopPointRef(){return this.stopPointRef;}
        private void setStopPointRef(String stopPointRef){this.stopPointRef = stopPointRef;}

        private String getVisitNumber(){return this.visitNumber;}
        private void setVisitNumber(String visitNumber){this.visitNumber = visitNumber;}

         String getVehicleAtStop(){return this.vehicleAtStop;}
         void setVehicleAtStop(String vehicleAtStop){this.vehicleAtStop = vehicleAtStop;}

         String getResponseTimestamp(){return this.responseTimestamp;}
         void setResponseTimestamp(String responseTimestamp){this.responseTimestamp = responseTimestamp;}

         String getValidUntil(){return this.validUntil;}
         void setValidUntil(String validUntil){this.validUntil = validUntil;}

         String getRecordedAtTime(){return this.recordedAtTime;}
         void setRecordedAtTime(String recordedAtTime){this.recordedAtTime = recordedAtTime;}

         String getLineRef() {return this.lineRef;}
         void setLineRef(String lineRef) {this.lineRef = lineRef;}

         String getDirectionRef(){return this.directionRef;}
         void setDirectionRef(String directionRef){this.directionRef = directionRef;}

         String getPublishedLineName(){return this.publishedLineName;}
         void setPublishedLineName(String publishedLineName){this.publishedLineName = publishedLineName;}

         String getDataFrameRef(){return this.dataFrameRef;}
         void setDataFrameRef(String dataFrameRef) {this.dataFrameRef = dataFrameRef;}

         String getDatedVehicleJourneyRef(){return this.datedVehicleJourneyRef;}
         void setDatedVehicleJourneyRef(String datedVehicleJourneyRef){this.datedVehicleJourneyRef = datedVehicleJourneyRef;}

         String getOriginRef(){return this.originRef;}
        void setOriginRef(String originRef){this.originRef = originRef;}

         String getDestinationRef(){return this.destinationRef;}
         void setDestinationRef(String destinationRef){this.destinationRef = destinationRef;}

         String getMonitored(){return this.monitored;}
         void setMonitored(String monitored){this.monitored = monitored;}

         String getLongitude(){return this.longitude;}
         void setLongitude(String longitude){this.longitude = longitude;}

         String getLatitude(){return this.latitude;}
         void setLatitude(String latitude){this.latitude = latitude;}

         String getProgressRate(){return this.progressRate;}
         void setProgressRate(String progressRate){this.progressRate = progressRate;}

         String getCourseOfJourneyRef(){return this.courseOfJourneyRef;}
         void setCourseOfJourneyRef(String courseOfJourneyRef){this.courseOfJourneyRef = courseOfJourneyRef;}

         String getVehicleRef(){return this.vehicleRef;}
         void setVehicleRef(String vehicleRef){this.vehicleRef = vehicleRef;}

         String getLastGPSFix(){return this.lastGPSFix;}
         void setLastGPSFix(String lastGPSFix){this.lastGPSFix = lastGPSFix;}

         String getScheduled(){return this.scheduled;}
         void setScheduled(String scheduled){this.scheduled = scheduled;}

         String getBearing(){return this.bearing;}
         void setBearing(String bearing){this.bearing = bearing;}

        String getSpeed(){return this.speed;}
         void setSpeed(String speed){this.speed = speed;}

         String getDestinationName() {return this.destinationName;}
         void setDestinationName(String destinationName){this.destinationName = destinationName;}
    }
}
