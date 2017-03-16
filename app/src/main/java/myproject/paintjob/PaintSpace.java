package myproject.paintjob;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PaintSpace extends AppCompatActivity {
    private static final String TAG = "PaintSpace";

    private RecyclerView colorList;
    private RecyclerView brushList;
    private ColorAdapter colorAdapter;
    private BrushAdapters brushAdapter;
    private TabLayout tabLayout;
    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_space);
        setUpRecyclerViews();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        drawView = (DrawView) findViewById(R.id.drawView);
    }

    private void setUpRecyclerViews() {
        colorList = (RecyclerView) findViewById(R.id.colorList);
        brushList = (RecyclerView) findViewById(R.id.brushList);

        colorAdapter = new ColorAdapter();
        brushAdapter = new BrushAdapters();

        colorList.setLayoutManager(new GridLayoutManager(PaintSpace.this, 6));
        brushList.setLayoutManager(new GridLayoutManager(PaintSpace.this, 6));

        colorList.setAdapter(colorAdapter);
        brushList.setAdapter(brushAdapter);
    }


    class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {
        int[] colors = {R.color.colorOne, R.color.colorTwo, R.color.colorThree, R.color.colorFour, R.color.colorFive,
                R.color.colorSix, R.color.colorSeven, R.color.colorEight, R.color.colorNine, R.color.colorTen, R.color.colorEleven,
                R.color.colorTwelve, R.color.colorThirteen, R.color.colorForteen, R.color.colorFifteen, R.color.colorSixteen,
                R.color.colorSeventeen, R.color.colorEighteen
        };

        @Override
        public ColorAdapter.ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ColorHolder(LayoutInflater.from(PaintSpace.this).inflate(R.layout.color_view, parent, false));
        }

        @Override
        public void onBindViewHolder(ColorAdapter.ColorHolder holder, int position) {
            holder.color.setBackgroundColor(getResources().getColor(colors[position]));
        }

        @Override
        public int getItemCount() {
            return colors.length;
        }

        class ColorHolder extends RecyclerView.ViewHolder {
            View color;

            public ColorHolder(View itemView) {
                super(itemView);
                color = itemView.findViewById(R.id.color);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setPaintColor(colors[getAdapterPosition()]);
                    }
                });
            }
        }
    }
    static float pxIntoDp(float px) {
        return  (px * Resources.getSystem().getDisplayMetrics().density + 0.5F);
    }
    class BrushAdapters extends RecyclerView.Adapter<BrushAdapters.BrushHolder> {
        float[] brushes = {4, 6, 8, 10, 12, 16};


        @Override
        public BrushAdapters.BrushHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BrushHolder(LayoutInflater.from(PaintSpace.this).inflate(R.layout.brush_view, parent, false));
        }

        @Override
        public void onBindViewHolder(final BrushAdapters.BrushHolder holder, int position) {


            Shape shape = new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {
                    paint.setColor(Color.BLACK);
                    paint.setAntiAlias(true);
                    paint.setStyle(Paint.Style.FILL);
                    float r = pxIntoDp(brushes[holder.getAdapterPosition()]);
                    float x = (canvas.getWidth()- r) / 2 ;
                    float y = (canvas.getHeight()- r) / 2;


                    RectF rectf = new RectF(0,0,r,r);
                    rectf.offset(x,y);
                  //  canvas.drawCircle(x, y, r, paint);
                    canvas.drawOval(rectf,paint);
                }
            };
            ShapeDrawable drawable = new ShapeDrawable();
            drawable.setShape(shape);
            holder.brush.setBackground(drawable);
        }

        @Override
        public int getItemCount() {
            return brushes.length;
        }

        class BrushHolder extends RecyclerView.ViewHolder {
            View brush;

            BrushHolder(View itemView) {
                super(itemView);
                brush = itemView.findViewById(R.id.brush);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setSize(brushes[getAdapterPosition()]);
                    }
                });
            }
        }
    }
}
