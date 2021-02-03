package com.example.workdaystracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workdaystracker.Adapter.DateAdapter;

import java.util.Date;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private DateAdapter adapter;

    public RecyclerItemTouchHelper(DateAdapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter= adapter;
    }

    @Override
    public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder holder, int direction){
        final int position = holder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Deleting Date");
            builder.setMessage("Are you sure you want to delete this workday?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(holder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            adapter.editItem(position);
        }
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder holder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c,
                recyclerView,
                holder,
                dX,
                dY,
                actionState,
                isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = holder.itemView;
        int backgroundCornerOffset = 20;
        if(dX>0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark));
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete_);
            background = new ColorDrawable(Color.RED);
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop() + (iconMargin);
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dX>0){
            //Swiping right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int)dX)  + backgroundCornerOffset, itemView.getBottom());
        } else if(dX<0){
            //Swiping left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight()-iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
