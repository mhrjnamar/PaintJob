package myproject.paintjob;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PaintSpace extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "PaintSpace";
    private static ArrayList<Item> items;

    // set up type of brushes
    static {

        // Smooth Corner Effect by using CornerPathEffect
        Paint cornerPaint = new Paint();
        cornerPaint.setStyle(Paint.Style.STROKE);
        cornerPaint.setPathEffect(new CornerPathEffect(pxIntoDp(10)));
        cornerPaint.setColor(Color.BLACK);
        cornerPaint.setAntiAlias(true);
        cornerPaint.setStrokeWidth(pxIntoDp(3));
        Path cornerPath = new Path();
        cornerPath.moveTo(pxIntoDp(12), pxIntoDp(24));
        cornerPath.lineTo(pxIntoDp(20), pxIntoDp(32));
        cornerPath.lineTo(pxIntoDp(28), pxIntoDp(18));
        cornerPath.lineTo(pxIntoDp(36), pxIntoDp(32));

        // Dash Effect by using DashPathEffect
        Paint dashPaint = new Paint();
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5f, 5f}, 30f));
        dashPaint.setColor(Color.BLACK);
        dashPaint.setAntiAlias(true);
        dashPaint.setStrokeWidth(pxIntoDp(3));
        Path dashPath = new Path();
        dashPath.moveTo(pxIntoDp(12), pxIntoDp(24));
        dashPath.lineTo(pxIntoDp(20), pxIntoDp(32));
        dashPath.lineTo(pxIntoDp(28), pxIntoDp(18));
        dashPath.lineTo(pxIntoDp(36), pxIntoDp(32));

        // Blur Effect using BlurMaskFilter
        Paint blurPaint = new Paint();
        blurPaint.setStyle(Paint.Style.FILL);
        blurPaint.setColor(Color.BLACK);
        blurPaint.setAntiAlias(true);
        blurPaint.setMaskFilter(new BlurMaskFilter(pxIntoDp(4), BlurMaskFilter.Blur.NORMAL));
        Path blurPath = new Path();
        blurPath.addCircle(pxIntoDp(23), pxIntoDp(23), pxIntoDp(10), Path.Direction.CW);

        // Emboss Effect using EmbossMaskFilter
        Paint embrossPaint = new Paint();
        embrossPaint.setStyle(Paint.Style.FILL);
        embrossPaint.setColor(Color.BLACK);
        embrossPaint.setAntiAlias(true);
        embrossPaint.setMaskFilter(new EmbossMaskFilter(new float[]{pxIntoDp(10), pxIntoDp(5), pxIntoDp(5)}, pxIntoDp(10), pxIntoDp(10), pxIntoDp(6)));
        Path embrossPath = new Path();
        embrossPath.addCircle(pxIntoDp(23), pxIntoDp(23), pxIntoDp(10), Path.Direction.CW);

        Paint trianglePaint = new Paint();
        trianglePaint.setStyle(Paint.Style.STROKE);
        trianglePaint.setColor(Color.BLACK);
        trianglePaint.setAntiAlias(true);
        trianglePaint.setStrokeWidth(pxIntoDp(1));
        Path trianglePath = new Path();
        trianglePath.moveTo(pxIntoDp(12), pxIntoDp(12));
        trianglePath.lineTo(pxIntoDp(12), pxIntoDp(48));
        trianglePath.lineTo(pxIntoDp(48), pxIntoDp(48));
        trianglePath.lineTo(pxIntoDp(48), pxIntoDp(12));

        trianglePaint.setPathEffect(new PathDashPathEffect(trianglePath, pxIntoDp(20), pxIntoDp(5), PathDashPathEffect.Style.TRANSLATE));

        items = new ArrayList<>();
        items.add(new Item(cornerPaint, cornerPath));
        items.add(new Item(dashPaint, dashPath));
        items.add(new Item(blurPaint, blurPath));
        items.add(new Item(embrossPaint, embrossPath));
        items.add(new Item(trianglePaint, trianglePath));
    }

    private RecyclerView colorList;
    private BrushTypeAdapter colorAdapter;
    private DrawView drawView;
    private BottomNavigationView bottomMenu;
    private LinearLayout selectionView, bottomSheet;
    private TextView clearAll;
    private BottomSheetBehavior behavior;
    private MenuItem undo, redo;

    static float pxIntoDp(float px) {
        return (px * Resources.getSystem().getDisplayMetrics().density + 0.5F);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_space);

        //initialize recyclerviews
        setUpRecyclerViews();

        drawView = (DrawView) findViewById(R.id.drawView);
        bottomMenu = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        selectionView = (LinearLayout) findViewById(R.id.selectionView);
        bottomSheet = (LinearLayout) findViewById(R.id.bottomSheet);
        clearAll = (TextView) findViewById(R.id.clearAll);

        drawView.setLayerType(View.LAYER_TYPE_NONE, drawView.getPaint());

        drawView.setOnClickListener(this);
        clearAll.setOnClickListener(this);

        bottomMenu.setOnNavigationItemSelectedListener(this);
        behavior = BottomSheetBehavior.from(bottomSheet);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        undo = menu.findItem(R.id.undo);
        redo = menu.findItem(R.id.redo);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.undo) {
            undo.setEnabled(drawView.undo());
            redo.setEnabled(true);
        } else if (item.getItemId() == R.id.redo) {
            redo.setEnabled(drawView.redo());
            undo.setEnabled(true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerViews() {
        colorList = (RecyclerView) findViewById(R.id.colorList);
        colorAdapter = new BrushTypeAdapter();
        colorList.setLayoutManager(new LinearLayoutManager(PaintSpace.this, LinearLayoutManager.HORIZONTAL, false));
        colorList.setAdapter(colorAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        behavior.setState(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
        clearAll.setVisibility(View.GONE);
        selectionView.setVisibility(View.GONE);
        colorList.setVisibility(View.GONE);
        switch (item.getItemId()) {
            case R.id.brush:
                selectionView.setVisibility(View.VISIBLE);
                colorList.setVisibility(View.VISIBLE);
                return true;
            case R.id.eraser:
                selectionView.setVisibility(View.VISIBLE);
                drawView.setPaintColor(android.R.color.white);
                return true;
            case R.id.clear:
                clearAll.setVisibility(View.VISIBLE);
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(clearAll)) {
            drawView.clearCanvas();
        } else if (v.equals(drawView)) {
            undo.setEnabled(true);
        }
    }

    private static class Item {
        Paint paint;
        Path path;


        public Item(Paint paint, Path path) {
            this.paint = paint;
            this.path = path;

        }
    }

    class BrushTypeAdapter extends RecyclerView.Adapter<BrushTypeAdapter.BrushHolder> {
        int[] colors = {R.color.colorFive, R.color.colorSix, R.color.colorSeven, R.color.colorThirteen, R.color.colorFive};

        @Override
        public BrushTypeAdapter.BrushHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BrushHolder(LayoutInflater.from(PaintSpace.this).inflate(R.layout.brush_style, parent, false));
        }

        @Override
        public void onBindViewHolder(BrushTypeAdapter.BrushHolder holder, int position) {
            Item item = items.get(position);
            holder.view.setPaint(item.paint, item.path);
            holder.view.setLayerType(View.LAYER_TYPE_SOFTWARE, item.paint);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class BrushHolder extends RecyclerView.ViewHolder {
            BrushTypeView view;

            public BrushHolder(View itemView) {
                super(itemView);
                view = (BrushTypeView) itemView.findViewById(R.id.brushType);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item item = items.get(getAdapterPosition());
                        item.paint.setStrokeWidth(pxIntoDp(5));
                        item.paint.setColor(getResources().getColor(colors[getAdapterPosition()]));
                        drawView.setPaint(item.paint);
                    }
                });
            }
        }
    }
}
