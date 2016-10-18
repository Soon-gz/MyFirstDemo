package com.abings.baby.data.remote.custom;

import com.abings.baby.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zwj on 2016/8/4.
 *
 * @param <T> 上文传下的参数
 * @param <P> 解析的对象
 */
public abstract class Func1Class<T, P> extends Func1Base<T> {
    private Class<P> clazz;

    public Func1Class(Class<P> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected Observable<T> callBase(JSONObject jsonObject) throws JSONException {
        List<P> ps = new ArrayList<>();
        if (jsonObject.has("result")) {
            String result = jsonObject.getString("result");
            ps = JsonUtils.fromJson(result, clazz);
            return callClass(ps);
        } else {
            return callClass(ps);
        }
    }

    /**
     * 成功的返回接口
     *
     * @param ps 解析结果
     * @return
     */
    protected abstract Observable<T> callClass(List<P> ps);

}
