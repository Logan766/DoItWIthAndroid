package com.janhoracek.doitwithandroid.Tasks;

import android.content.Context;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateHandler;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.R;
import com.tooltip.Tooltip;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.rgb;
/**
 * Adapter for recycler view containing all current Tasks
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class TaskAdapterAll extends ListAdapter<Taskers, TaskAdapterAll.TaskHolder> {
    private OnTaskClickListener mListener;
    private Context mContext;

    /**
     * Constructor
     *
     * @param context Context
     */
    public TaskAdapterAll(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    public static final DiffUtil.ItemCallback<Taskers> DIFF_CALLBACK = new DiffUtil.ItemCallback<Taskers>() {
        @Override
        public boolean areItemsTheSame(@NonNull Taskers oldItem, @NonNull Taskers newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Taskers oldItem, @NonNull Taskers newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority() &&
                    oldItem.getTime_consumption() == newItem.getTime_consumption() &&
                    oldItem.getD_time_milisec() == newItem.getD_time_milisec() &&
                    oldItem.getExp() == newItem.getExp() &&
                    oldItem.getD_day() == newItem.getD_day() &&
                    oldItem.getD_month() == newItem.getD_month() &&
                    oldItem.getD_year() == newItem.getD_year() &&
                    oldItem.getId() == newItem.getId() &&
                    oldItem.getD_time().equals(newItem.getD_time()) &&
                    oldItem.isDoable_all() == newItem.isDoable_all() &&
                    oldItem.isDoable_medium() == newItem.isDoable_medium() &&
                    oldItem.isDoable_high() == oldItem.isDoable_high() &&
                    (oldItem.getD_time_milisec() < new DateHandler().getCurrentDateTimeInMilisec()) == (newItem.getD_time_milisec() < new DateHandler().getCurrentDateTimeInMilisec());

        }


    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_all, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mContext.getString(R.string.dateFormat));
        Taskers currentTaskers = getItem(position);
        //change icon based on deadline and color priority
        switch (currentTaskers.getPriority()) {
            case 1:
                holder.mPriority.setBackgroundColor(rgb(239, 83, 80));
                holder.mProgress.getProgressDrawable().setColorFilter(rgb(239, 83, 80), android.graphics.PorterDuff.Mode.SRC_IN);
                if(!DataHolder.getInstance().getHighTasksDoable()) {
                    if(!currentTaskers.isDoable_high()) {
                        holder.mLottieUndoable.setVisibility(View.VISIBLE);
                    } else {
                        holder.mLottieUndoable.setVisibility(View.GONE);
                    }
                } else {
                    holder.mLottieUndoable.setVisibility(View.GONE);
                    holder.mTextViewFire.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                holder.mPriority.setBackgroundColor(rgb(255,202,40));
                holder.mProgress.getProgressDrawable().setColorFilter(rgb(255,202,40), android.graphics.PorterDuff.Mode.SRC_IN);
                if(!DataHolder.getInstance().getMediumTasksDoable()) {
                    if(!currentTaskers.isDoable_medium()) {
                        holder.mLottieUndoable.setVisibility(View.VISIBLE);
                    } else {
                        holder.mLottieUndoable.setVisibility(View.GONE);
                    }
                } else {
                    holder.mLottieUndoable.setVisibility(View.GONE);
                    holder.mTextViewFire.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                holder.mPriority.setBackgroundColor(rgb(156,204,101));
                holder.mProgress.getProgressDrawable().setColorFilter(rgb(156,204,101), android.graphics.PorterDuff.Mode.SRC_IN);
                if(!DataHolder.getInstance().getAllTasksDoable()) {
                    if(!currentTaskers.isDoable_all()) {
                        holder.mLottieUndoable.setVisibility(View.VISIBLE);
                    } else {
                        holder.mLottieUndoable.setVisibility(View.GONE);
                    }
                } else {
                    holder.mLottieUndoable.setVisibility(View.GONE);
                    holder.mTextViewFire.setVisibility(View.VISIBLE);
                }
                break;
        }


        if(!DataHolder.getInstance().getMediumTasksDoable()) {
            if(currentTaskers.getPriority() == 1) {
                holder.mLinearLayoutDoableAll.setVisibility(View.VISIBLE);
            } else {
                holder.mLinearLayoutDoableAll.setVisibility(View.GONE);
            }
        } else if(!DataHolder.getInstance().getAllTasksDoable()) {
            if(currentTaskers.getPriority() < 3) {
                holder.mLinearLayoutDoableAll.setVisibility(View.VISIBLE);
            } else {
                holder.mLinearLayoutDoableAll.setVisibility(View.GONE);
            }
        } else {
            holder.mLinearLayoutDoableAll.setVisibility(View.GONE);
        }

        //change icon if task is after its deadline
        if(currentTaskers.getD_time_milisec() < new DateHandler().getCurrentDateTimeInMilisec()) {
            holder.mTextViewFire.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_whatshot_red_24dp), null, null, null);
        } else {
            switch (currentTaskers.getPriority()) {
                case 1:
                    if(!DataHolder.getInstance().getHighTasksDoable()) {
                        if(!currentTaskers.isDoable_high()) {
                            holder.mLottieUndoable.setVisibility(View.VISIBLE);
                            holder.mTextViewFire.setVisibility(View.INVISIBLE);
                        } else {
                            holder.mLottieUndoable.setVisibility(View.GONE);
                            holder.mTextViewFire.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 2:
                    if(!DataHolder.getInstance().getMediumTasksDoable()) {
                        if(!currentTaskers.isDoable_medium()) {
                            holder.mLottieUndoable.setVisibility(View.VISIBLE);
                            holder.mTextViewFire.setVisibility(View.INVISIBLE);
                        } else {
                            holder.mLottieUndoable.setVisibility(View.GONE);
                            holder.mTextViewFire.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 3:
                    if(!DataHolder.getInstance().getAllTasksDoable()) {
                        if(!currentTaskers.isDoable_all()) {
                            holder.mLottieUndoable.setVisibility(View.VISIBLE);
                            holder.mTextViewFire.setVisibility(View.INVISIBLE);
                        } else {
                            holder.mLottieUndoable.setVisibility(View.GONE);
                            holder.mTextViewFire.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
            holder.mTextViewFire.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_whatshot_black_24dp), null, null, null);
        }


        holder.mTextViewTitle.setText(currentTaskers.getName());
        holder.mTextViewDescription.setText(currentTaskers.getDescription());
        holder.mTextViewEstTime.setText(String.valueOf(currentTaskers.getTime_consumption()));
        holder.mTextViewDeadline.setText(dateFormat.format(currentTaskers.getD_time_milisec()));


        if(currentTaskers.getCompleted() > 0) {
            holder.mTextViewCompleted.setVisibility(View.VISIBLE);
            holder.mTextViewCompleted.setText(String.valueOf(Math.round(currentTaskers.getCompleted() / (float) currentTaskers.getTime_consumption() * 100)) + "%");
            holder.mTextViewCompletedIco.setVisibility(View.VISIBLE);
            holder.mProgress.setProgress(Math.round(currentTaskers.getCompleted() / (float) currentTaskers.getTime_consumption() * 100));
        } else {
            holder.mProgress.setProgress(0);
            holder.mTextViewCompletedIco.setVisibility(View.INVISIBLE);
            holder.mTextViewCompleted.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Gets task at given position
     *
     * @param position position of task
     * @return Task
     */
    public Taskers getTaskAt(int position) {
        return getItem(position);
    }

    /**
     * ViewHolder for Task
     */
    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private RelativeLayout mRelativeLayout;
        private TextView mTextViewDeadline;
        private TextView mTextViewEstTime;
        private LinearLayout mPriority;
        private TextView mTextViewFire;
        private TextView mTextViewCompletedIco;
        private TextView mTextViewCompleted;
        private ProgressBar mProgress;
        private LinearLayout mLinearLayoutDoableAll;
        private LottieAnimationView mLottieUndoable;
        private Tooltip mTooltipDeadline;
        private Tooltip mTooltipFocus;


        public TaskHolder(@NonNull final View itemView) {
            super(itemView);
            mRelativeLayout = itemView.findViewById(R.id.task_backgroud);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
            mTextViewDeadline = itemView.findViewById(R.id.text_view_deadline_data);
            mTextViewEstTime = itemView.findViewById(R.id.text_view_time_consumption_data);
            mPriority = itemView.findViewById(R.id.priority);
            mTextViewFire = itemView.findViewById(R.id.text_view_deadline);
            mTextViewCompletedIco = itemView.findViewById(R.id.text_view_completed_ico);
            mTextViewCompleted = itemView.findViewById(R.id.text_view_completed_data);
            mProgress = itemView.findViewById(R.id.progressBar2);
            mLinearLayoutDoableAll = itemView.findViewById(R.id.lin_layout_all);
            mLottieUndoable = itemView.findViewById(R.id.lottie_unodable_task);

            final Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE) ;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        mListener.onTaskClick(getItem(position));
                        vibe.vibrate(50);
                    }
                    return true;
                }
            });

            mTooltipDeadline = new Tooltip.Builder(mLottieUndoable)
                    .setText(mContext.getString(R.string.task_adapter_deadline_not_completable))
                    .setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent))
                    .setTextColor(mContext.getResources().getColor(R.color.backgroundNormal))
                    .setDismissOnClick(true)
                    .setCornerRadius(20f)
                    .setCancelable(true)
                    .setGravity(Gravity.RIGHT)
                    .build();


            mLottieUndoable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTooltipDeadline.show();
                }
            });

            mTooltipFocus = new Tooltip.Builder(mLinearLayoutDoableAll)
                    .setText(mContext.getString(R.string.task_adapter_focus_task))
                    .setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent))
                    .setTextColor(mContext.getResources().getColor(R.color.backgroundNormal))
                    .setDismissOnClick(true)
                    .setCornerRadius(20f)
                    .setCancelable(true)
                    .setGravity(Gravity.LEFT)
                    .build();
            mLinearLayoutDoableAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTooltipFocus.show();
                }
            });
        }
    }

    /**
     * Interface to make Task clickable
     */
    public interface OnTaskClickListener {
        void onTaskClick(Taskers task);
    }

    /**
     * Sets click listener on Task
     * @param listener listener
     */
    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.mListener = listener;
    }
}
