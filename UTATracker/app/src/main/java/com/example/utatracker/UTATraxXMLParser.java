package com.example.utatracker;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UTATraxXMLParser {
    // We don't use namespaces
    private static final String ns = null;

    public static List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
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
    private static List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List vehiclesByRoute = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for MonitoredVehicleJourney Tag
            if (name.equals("MonitoredVehicleJourney")) {
                vehiclesByRoute.add(readVehicle(parser));
            } /*else {
                //Skips unwanted entries
                skip(parser);
            }*/
        }
        return vehiclesByRoute;
    }

    public static class MonitoredVehicleByRoute {
        //Represent a <tag> description from the returned XML.
        public final String dataFrameRef;
        public final String datedVehicleJourneyRef;
        public final String publishedLineName;
        public final String originRef;
        public final String destinationRef;
        public final String lineRef;
        public final String directionRef;
        public final String monitored;
        public final String longitude;
        public final String latitude;
        public final String progressRate;
        public final String courseOfJourneyRef;
        public final String vehicleRef;
        public final String lastGPSFix;
        public final String scheduled;
        public final String bearing;
        public final String speed;
        public final String destinationName;

        private MonitoredVehicleByRoute(String dataFrameRef,String datedVehicleJourneyRef ,String publishedLineName, String originRef, String destinationRef,
                                        String lineRef, String directionRef,String monitored ,String longitude, String latitude,String progressRate, String courseOfJourneyRef,
                                        String vehicleRef, String lastGPSFix, String scheduled, String bearing, String speed, String destinationName ) {
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
            this.lastGPSFix = lastGPSFix;
            this.scheduled = scheduled;
            this.bearing = bearing;
            this.speed = speed;
            this.destinationName = destinationName;
        }
        private String getLineRef() {return this.lineRef;}
        private String getDirectionRef(){return this.directionRef;}
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.

    /**
     * Parses the contents of a monitored vehicle. If it encounters a LineRef,DirectionRef,FramedVehicleJourneyRef,PublishedLineName,
     * OriginRef,DestinationRef, Monitored, VehicleLocation, ProgressRate, CourseOfJourneyRef, VehicleRef or Extensions tag,
     * handing them off to their 'read' methods for processing; otherwise skips the tag.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static MonitoredVehicleByRoute readVehicle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "MonitoredVehicleJourney");
        String dataFrameRef = null;
        String datedVehicleJourneyRef = null;
        String publishedLineName = null;
        String originRef = null;
        String destinationRef = null;
        String lineRef = null;
        String directionRef = null;
        String monitored = null;
        String longitude = null;
        String latitude = null;
        String progressRate = null;
        String courseOfJourneyRef = null;
        String vehicleRef = null;
        String lastGPSFix = null;
        String scheduled = null;
        String bearing = null;
        String speed = null;
        String destinationName = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("LineRef")) {
                lineRef = readLineRef(parser);
            } else if (name.equals("DirectionRef")) {
                directionRef = readDirectionRef(parser);
            }
            /*else if (name.equals("FramedVehicleJourneyRef")) {
                dataFrameRef = readDataFrameRef(parser);
                datedVehicleJourneyRef = readDatedVehicleJourneyRef(parser);
            }  else if (name.equals("PublishedLineName")) {
                publishedLineName = readPublishedLineName(parser);
            } else if (name.equals("OriginRef")) {
                originRef = readOriginRef(parser);
            } else if (name.equals("DestinationRef")) {
                destinationRef = readDestinationRef(parser);
            } else if (name.equals("Monitored")) {
                monitored = readMonitored(parser);
            } else if (name.equals("VehicleLocation")) {
                longitude = readLongitude(parser);
                latitude = readLatitude(parser);
            } else if (name.equals("ProgressRate")) {
                progressRate = readProgressRate(parser);
            } else if (name.equals("CourseOfJourneyRef")) {
                courseOfJourneyRef = readCourseOfJourneyRef(parser);
            } else if (name.equals("VehicleRef")) {
                vehicleRef = readVehicleRef(parser);
            } else if (name.equals("Extensions")) {
                lastGPSFix = readLastGPSFix(parser);
                scheduled = readScheduled(parser);
                bearing = readBearing(parser);
                speed = readSpeed(parser);
                destinationName = readDestinationName(parser);
            }*/ else {
                skip(parser);
            }
        }
        return new MonitoredVehicleByRoute( dataFrameRef, datedVehicleJourneyRef , publishedLineName,  originRef,  destinationRef,
                lineRef,  directionRef,monitored ,longitude,  latitude, progressRate,  courseOfJourneyRef,
                vehicleRef,  lastGPSFix,  scheduled,  bearing,  speed,  destinationName);
    }


    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     *  Processes LineRef tag from the feed.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readLineRef(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String lineRef = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return lineRef;
    }

    private static String readDirectionRef(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "DirectionRef");
        String directionRef = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return directionRef;
    }


    /**
     * Extracts the text values from a tag.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
