package com.example.utatracker;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class UTATraxXMLParser {
    // We don't use namespaces
    private static final String ns = null;

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
     *
     * The readFeed() method does the actual work of processing the feed.
     * It looks for elements tagged "entry" as a starting point for recursively processing the feed. If a tag isn't an entry tag, it skips it.
     * Once the whole feed has been recursively processed, readFeed() returns a List containing the entries (including nested data members)
     * it extracted from the feed. This List is then returned by the parser.
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
                    } else if(tagName.equalsIgnoreCase("ResponseTimestamp")){
                        v.setResponseTimestamp(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("ValidUntil")){
                        v.setValidUntil(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("RecordedAtTime")){
                        v.setRecordedAtTime(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("LineRef")){
                        v.setLineRef(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("DirectionRef")){
                        v.setDirectionRef(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("DataFrameRef")){
                        v.setDataFrameRef(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("DatedVehicleJourneyRef")){
                        v.setDatedVehicleJourneyRef(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("PublishedLineName")){
                        v.setPublishedLineName(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("OriginRef")){
                        v.setOriginRef(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("DestinationRef")){
                        v.setDestinationRef(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("Monitored")){
                        v.setMonitored(parser.nextText());
                        eventType = parser.next();
                    } else if(tagName.equalsIgnoreCase("Longitude")){
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
                        v.setStopPointRef(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("VisitNumber")){
                        v.setVisitNumber(parser.nextText());
                        eventType = parser.next();
                    }else if(tagName.equalsIgnoreCase("VehicleAtStop")){
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
                case XmlPullParser.IGNORABLE_WHITESPACE:
                    eventType = parser.next();
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

        //Constructor with params
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
        //Constructor no params
        private MonitoredVehicleByRoute(){

        }

        private String getStopPointRef(){return this.stopPointRef;}
        private void setStopPointRef(String stopPointRef){this.stopPointRef = stopPointRef;}

        private String getVisitNumber(){return this.visitNumber;}
        private void setVisitNumber(String visitNumber){this.visitNumber = visitNumber;}

        private String getVehicleAtStop(){return this.vehicleAtStop;}
        private void setVehicleAtStop(String vehicleAtStop){this.vehicleAtStop = vehicleAtStop;}

        private String getResponseTimestamp(){return this.responseTimestamp;}
        private void setResponseTimestamp(String responseTimestamp){this.responseTimestamp = responseTimestamp;}

        private String getValidUntil(){return this.validUntil;}
        private void setValidUntil(String validUntil){this.validUntil = validUntil;}

        private String getRecordedAtTime(){return this.recordedAtTime;}
        private void setRecordedAtTime(String recordedAtTime){this.recordedAtTime = recordedAtTime;}

        private String getLineRef() {return this.lineRef;}
        private void setLineRef(String lineRef) {this.lineRef = lineRef;}

        private String getDirectionRef(){return this.directionRef;}
        private void setDirectionRef(String directionRef){this.directionRef = directionRef;}

        private String getPublishedLineName(){return this.publishedLineName;}
        private void setPublishedLineName(String publishedLineName){this.publishedLineName = publishedLineName;}

        private String getDataFrameRef(){return this.dataFrameRef;}
        private void setDataFrameRef(String dataFrameRef) {this.dataFrameRef = dataFrameRef;}

        private String getDatedVehicleJourneyRef(){return this.datedVehicleJourneyRef;}
        private void setDatedVehicleJourneyRef(String datedVehicleJourneyRef){this.datedVehicleJourneyRef = datedVehicleJourneyRef;}

        private String getOriginRef(){return this.originRef;}
        private void setOriginRef(String originRef){this.originRef = originRef;}

        private String getDestinationRef(){return this.destinationRef;}
        private void setDestinationRef(String destinationRef){this.destinationRef = destinationRef;}

        private String getMonitored(){return this.monitored;}
        private void setMonitored(String monitored){this.monitored = monitored;}

        private String getLongitude(){return this.longitude;}
        private void setLongitude(String longitude){this.longitude = longitude;}

        private String getLatitude(){return this.latitude;}
        private void setLatitude(String latitude){this.latitude = latitude;}

        private String getProgressRate(){return this.progressRate;}
        private void setProgressRate(String progressRate){this.progressRate = progressRate;}

        private String getCourseOfJourneyRef(){return this.courseOfJourneyRef;}
        private void setCourseOfJourneyRef(String courseOfJourneyRef){this.courseOfJourneyRef = courseOfJourneyRef;}

        private String getVehicleRef(){return this.vehicleRef;}
        private void setVehicleRef(String vehicleRef){this.vehicleRef = vehicleRef;}

        private String getLastGPSFix(){return this.lastGPSFix;}
        private void setLastGPSFix(String lastGPSFix){this.lastGPSFix = lastGPSFix;}

        private String getScheduled(){return this.scheduled;}
        private void setScheduled(String scheduled){this.scheduled = scheduled;}

        private String getBearing(){return this.bearing;}
        private void setBearing(String bearing){this.bearing = bearing;}

        private String getSpeed(){return this.speed;}
        private void setSpeed(String speed){this.speed = speed;}

        private String getDestinationName() {return this.destinationName;}
        private void setDestinationName(String destinationName){this.destinationName = destinationName;}
    }
}
