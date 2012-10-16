package pt.esper.listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.UpdateListener;

public class DebugEventListener implements UpdateListener {
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    public void update(EventBean[] newEvents, EventBean[] oldEvents) {

        if(newEvents != null && newEvents.length != 0)
        {
            for(EventBean newEvent : newEvents)
            {
                System.out.println("Debug:" + dateFormat.format(Calendar.getInstance().getTime()) + ":" + newEvent.getEventType().getName() + "::" + newEvent.getUnderlying().toString() );
            }
        }
    }

}
