package ph.edu.usc.beautyapp2;




import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Xml;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import java.io.StringReader;

public class XMLDisplayActivity extends AppCompatActivity {

    private TextView xmlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmldisplay);

        xmlTextView = findViewById(R.id.xml_text_view);

        // Read the XML from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        String cartXML = sharedPreferences.getString("cartXML", "No cart data found.");

        // Parse and display the XML
        String parsedData = parseXML(cartXML);
        xmlTextView.setText(parsedData);
    }

    private String parseXML(String xml) {
        StringBuilder parsedData = new StringBuilder();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "product".equals(parser.getName())) {
                    String name = parser.getAttributeValue(null, "name");
                    String price = parser.getAttributeValue(null, "price");
                    parsedData.append("Product: ").append(name).append(", Price: $").append(price).append("\n");
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing XML";
        }
        return parsedData.toString();
    }
}