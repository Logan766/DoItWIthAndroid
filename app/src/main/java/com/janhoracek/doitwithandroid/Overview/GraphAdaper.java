package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Adapter for recyclew view in statistics
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class GraphAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ChartItem> items;
    Context mContext;

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getGraphType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        LayoutInflater inflater = null;
        View view = null;
        switch (viewType) {
            case 1:
                inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.list_item_linechart, parent, false);
                return new GraphHolderLineChart(view);
            case 2:
                inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.list_item_barchart, parent, false);
                return new GraphHolderBarChart(view);
            case 3:
                inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.list_item_piechart, parent, false);
                return new GraphHolderPieChart(view);
            default:
                return null;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 1:
                    {
                    GraphHolderLineChart viewHolder = (GraphHolderLineChart) holder;
                    LineChart chart = ((GraphHolderLineChart) holder).graph;
                    ChartItem item = items.get(position);
                    viewHolder.setIsRecyclable(true);

                    viewHolder.lineTitle.setText(item.getTitle());

                    item.styleGraph(viewHolder.graph);
                    item.setGraphData(DataHolder.getInstance().getmLineChartData());

                    chart.invalidate();
                    chart.animate();

                    }
                    break;
                case 2:
                    {
                    GraphHolderBarChart viewHolder = (GraphHolderBarChart) holder;
                    BarChart chart = ((GraphHolderBarChart) holder).graph;
                    ChartItem item = items.get(position);
                    viewHolder.setIsRecyclable(true);

                    viewHolder.barTitle.setText(item.getTitle());
                    item.styleGraph(viewHolder.graph);
                        switch (item.getOwnKindType()) {
                            case 1:
                                item.setGraphData(DataHolder.getInstance().getmBarDataDay());
                                break;
                            case 2:
                                item.setGraphData(DataHolder.getInstance().getmBarDataMonth());
                                break;
                        }
                    chart.getData().setHighlightEnabled(false);
                    chart.invalidate();
                    chart.animate();
                    }
                    break;
                case 3:
                {
                    GraphHolderPieChart viewHolder = (GraphHolderPieChart) holder;
                    ChartItem item = items.get(position);
                    PieChart chart = ((GraphHolderPieChart) holder).graph;
                    viewHolder.setIsRecyclable(true);

                    viewHolder.pieTitle.setText(item.getTitle());
                    item.styleGraph(viewHolder.graph);

                    item.setGraphData(DataHolder.getInstance().getmPieOverallData());
                    chart.getDescription().setEnabled(false);

                    chart.invalidate();
                    chart.animate();
                }
                    break;
                default:
                    break;
            }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Sets graphs
     *
     * @param graphs list of ChartItems
     */
    public void setGraphs (List<ChartItem> graphs) {
        this.items = graphs;
    }

    /**
     * Get graphs
     *
     * @return List of ChartItems
     */
    public List<ChartItem> getGraphs() {
        return items;
    }

    /**
     * ViewHolder for BarChart
     */
    class GraphHolderBarChart extends  RecyclerView.ViewHolder {

        public BarChart graph;
        public TextView barTitle;

        public GraphHolderBarChart(@NonNull View itemView) {
            super(itemView);
            this.graph = itemView.findViewById(R.id.bar_chart_overview);
            this.barTitle = itemView.findViewById(R.id.item_barchart_text_view_title);
        }
    }

    /**
     * ViewHolder for LineChart
     */
    class GraphHolderLineChart extends  RecyclerView.ViewHolder {

        public LineChart graph;
        public TextView lineTitle;

        public GraphHolderLineChart(@NonNull View itemView) {
            super(itemView);
            graph = itemView.findViewById(R.id.linechart_overview);
            lineTitle = itemView.findViewById(R.id.item_linechart_text_view_title);
        }
    }

    /**
     * ViewHolder for PieChart
     */
    class GraphHolderPieChart extends  RecyclerView.ViewHolder {

        public PieChart graph;
        public TextView pieTitle;

        public GraphHolderPieChart(@NonNull View itemView) {
            super(itemView);
            graph = itemView.findViewById(R.id.piechart_overview);
            pieTitle = itemView.findViewById(R.id.item_piechart_text_view_title);
        }
    }

}
