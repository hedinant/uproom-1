package ru.uproom.gate.devices.zwave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwave4j.Manager;
import org.zwave4j.ValueId;
import ru.uproom.gate.notifications.zwave.NotificationWatcherImpl;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by osipenko on 14.08.14.
 */
public class ZWaveValue {


    //=============================================================================================================
    //======    fields

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWatcherImpl.class);

    private int id;
    private ValueId valueId;


    //=============================================================================================================
    //======    constructors


    public ZWaveValue(ValueId valueId) {
        this.valueId = valueId;
        this.id = ZWaveValueIndexFactory.createIndex(valueId);
    }


    //=============================================================================================================
    //======    getters & setters


    //------------------------------------------------------------------------
    //  z-wave value label

    public String getValueLabel() {
        return Manager.get().getValueLabel(valueId);
    }


    //------------------------------------------------------------------------
    //  gate value id

    public int getId() {
        return id;
    }


    //=============================================================================================================
    //======    methods


    //------------------------------------------------------------------------
    //  get parameter value

    private Object getValue() {
        switch (valueId.getType()) {
            case BOOL:
                AtomicReference<Boolean> b = new AtomicReference<>();
                Manager.get().getValueAsBool(valueId, b);
                return b.get();
            case BYTE:
                AtomicReference<Short> bb = new AtomicReference<>();
                Manager.get().getValueAsByte(valueId, bb);
                return bb.get();
            case DECIMAL:
                AtomicReference<Float> f = new AtomicReference<>();
                Manager.get().getValueAsFloat(valueId, f);
                return f.get();
            case INT:
                AtomicReference<Integer> i = new AtomicReference<>();
                Manager.get().getValueAsInt(valueId, i);
                return i.get();
            case LIST:
                return null;
            case SCHEDULE:
                return null;
            case SHORT:
                AtomicReference<Short> s = new AtomicReference<>();
                Manager.get().getValueAsShort(valueId, s);
                return s.get();
            case STRING:
                AtomicReference<String> ss = new AtomicReference<>();
                Manager.get().getValueAsString(valueId, ss);
                return ss.get();
            case BUTTON:
                return null;
            case RAW:
                AtomicReference<short[]> sss = new AtomicReference<>();
                Manager.get().getValueAsRaw(valueId, sss);
                return sss.get();
            default:
                return null;
        }
    }


    //------------------------------------------------------------------------
    //  get parameter value as string

    public String getValueAsString() {
        Object obj = getValue();
        return (obj == null) ? "null" : obj.toString();
    }


    //------------------------------------------------------------------------
    //  parameter information as string

    @Override
    public String toString() {

        return String.format("{\"id\":\"%d\",\"label\":\"%s\",\"value\":\"%s\"}",
                id,
                getValueLabel(),
                getValueAsString()
        );
    }


    //------------------------------------------------------------------------
    //  set parameter value as string

    public boolean setValue(String value) {
        LOG.debug(">>>> set value : " + this.toString() + " to value : " + value);
        return Manager.get().setValueAsString(valueId, value);
    }
}