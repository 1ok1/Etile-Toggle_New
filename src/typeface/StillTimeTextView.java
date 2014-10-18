package typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lokesh.d on 28-08-2014.
 */
public class StillTimeTextView extends TextView {

    public StillTimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public StillTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public StillTimeTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/still time.ttf");
                setTypeface(myTypeface);
        }


}
