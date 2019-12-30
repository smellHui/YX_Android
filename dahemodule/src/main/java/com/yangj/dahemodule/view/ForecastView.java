package com.yangj.dahemodule.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.xuncha.Forecast;
import com.yangj.dahemodule.model.xuncha.Weather;

import static com.yangj.dahemodule.model.xuncha.Weather.CLOUDY;
import static com.yangj.dahemodule.model.xuncha.Weather.PERIODIC_CLOUDS;

/**
 * Author:xch
 * Date:2019/11/19
 * Description:
 */
public class ForecastView extends LinearLayout {

    private Paint gradientPaint;
    private int[] currentGradient;

    private ArgbEvaluator evaluator;

    public ForecastView(Context context) {
        super(context);
    }

    public ForecastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ForecastView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ForecastView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    {
        evaluator = new ArgbEvaluator();
        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWillNotDraw(false);
    }

    private void initGradient() {
        float centerX = getWidth() * 0.5f;
        Shader gradient = new LinearGradient(
                centerX, 0, centerX, getHeight(),
                currentGradient, null,
                Shader.TileMode.MIRROR);
        gradientPaint.setShader(gradient);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (currentGradient != null)
            initGradient();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), gradientPaint);
        super.onDraw(canvas);
    }

    public void setForecast(RoutePosition routePosition) {
//        currentGradient = weatherToGradient(forecast.getWeather());
        currentGradient = weatherToGradient(routePosition);
        if (getWidth() != 0 && getHeight() != 0) {
            initGradient();
        }
        invalidate();
    }

    public void onScroll(float fraction, RoutePosition oldF, RoutePosition newF) {
        currentGradient = mix(fraction,
                weatherToGradient(newF),
                weatherToGradient(oldF));
        initGradient();
        invalidate();
    }

    private int[] weatherToGradient(RoutePosition item) {
        int notCompletedNum = SqlManager.getInstance().getNotCompletedTaskById(item.getWorkOrderId(), item.getPositionId());
        if (item.getIsNear() == 1){
            return colors(R.array.gradientPeriodicClouds);
        }else {
            if (notCompletedNum != 0){
                return colors(R.array.gradientCloudy);
            }else {
                return colors(R.array.gradientMostlyCloudy);
            }
        }
    }

    private int[] mix(float fraction, int[] c1, int[] c2) {
        return new int[]{
                (Integer) evaluator.evaluate(fraction, c1[0], c2[0]),
                (Integer) evaluator.evaluate(fraction, c1[1], c2[1]),
                (Integer) evaluator.evaluate(fraction, c1[2], c2[2])
        };
    }

    private int[] colors(@ArrayRes int res) {
        return getContext().getResources().getIntArray(res);
    }

}
