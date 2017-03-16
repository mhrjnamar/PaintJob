package myproject.paintjob;

import android.graphics.Paint;
import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by User on 3/13/2017.
 */

public class DrawDetails implements Serializable {
    private Paint paint;
    private Path path;

    public DrawDetails(Paint paint, Path path) {
        this.paint = paint;
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
