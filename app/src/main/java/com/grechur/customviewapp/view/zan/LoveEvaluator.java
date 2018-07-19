package com.grechur.customviewapp.view.zan;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by zz on 2018/6/6.
 */

public class LoveEvaluator implements TypeEvaluator<PointF> {

    private PointF p1, p2;
    public LoveEvaluator(PointF p1,PointF p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public PointF evaluate(float t, PointF p0, PointF p3) {
        //t是0~1的范围 套公式
        PointF pointF = new PointF();
        //三阶贝塞尔公式
        pointF.x = p0.x*(1-t)*(1-t)*(1-t)
                +3*p1.x*t*(1-t)*(1-t)
                +3*p2.x*t*t*(1-t)
                +p3.x*t*t*t;
        pointF.y = p0.y*(1-t)*(1-t)*(1-t)
                +3*p1.y*t*(1-t)*(1-t)
                +3*p2.y*t*t*(1-t)
                +p3.y*t*t*t;
        return pointF;
    }
}
