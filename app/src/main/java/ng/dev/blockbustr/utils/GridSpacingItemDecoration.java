package ng.dev.blockbustr.utils;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private int totalItems;

    public GridSpacingItemDecoration(int spanCount, int spacing, int totalItems) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.totalItems = totalItems;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

//        outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//        outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (column == 1) {
            outRect.left = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, parent.getResources().getDisplayMetrics()));
        }
//
//        if (position > spanCount - 1) { // top edge
//            outRect.top = spacing / 3;
//        } else {
//            outRect.top = 0;
//        }


        // item bottom
//        if (includeEdge) {
//
//        }
//        else {
//            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//            if (position >= spanCount) {
//                outRect.top = spacing; // item top
//            }
//        }
    }
}
