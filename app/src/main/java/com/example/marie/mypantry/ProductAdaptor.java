package com.example.marie.mypantry;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Vector;

public class ProductAdaptor extends ArrayAdapter<Product>{

    Context context;
    int resource;

    Product currentProduct;
    Vector<Product> data;

    public ProductAdaptor(@NonNull Context context, int resource, Vector<Product> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StringReadHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new StringReadHolder();

            holder.productName = (TextView) row.findViewById(R.id.product_name);
            holder.productAmount = (TextView) row.findViewById(R.id.product_amount);
            holder.amountUnit = (TextView) row.findViewById(R.id.product_amount_unit);
            holder.productLocation = (TextView) row.findViewById(R.id.product_location);
            holder.deleteButton = (Button) row.findViewById(R.id.product_delete);
            row.setTag(holder);
        } else {
            holder = (StringReadHolder) row.getTag();
        }

        Product p = data.elementAt(position);

        holder.productName.setText(p.getProductName());
        holder.productAmount.setText(Double.toString(p.getAmount()));
        holder.amountUnit.setText(p.getAmountUnit());
        holder.productLocation.setText(p.getLocation());

        return row;
    }

    static class StringReadHolder {
        // for if it also starts to contain ImageView on top of TextViews
        // ex: photo/sketch of product > TODO
        TextView productName, productAmount, amountUnit, productLocation;
        Button deleteButton;
    }
}
