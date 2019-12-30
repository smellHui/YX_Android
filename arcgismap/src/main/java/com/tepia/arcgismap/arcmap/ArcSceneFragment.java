package com.tepia.arcgismap.arcmap;


import android.databinding.DataBindingUtil;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.io.RequestConfiguration;
import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.tepia.arcgismap.R;
import com.tepia.arcgismap.databinding.FragmentArcMapBinding;
import com.tepia.arcgismap.databinding.FragmentArcSceneBinding;
import com.tepia.arcgismap.tianditu.TianDiTuMethodsClass;
//import com.tepia.base.arouter.AppRoutePath;
import com.tepia.base.AppRoutePath;
import com.tepia.base.mvp.MVPBaseFragment;

/**
 * @author :       zhang xinhua
 * @Version :       1.0
 * @创建人 ：      zhang xinhua
 * @创建时间 :       2019/6/13 15:44
 * @修改人 ：
 * @修改时间 :       2019/6/13 15:44
 * @功能描述 :
 **/
//@Route(path = AppRoutePath.app_fragment_arc_scene)
public class ArcSceneFragment extends MVPBaseFragment<ArcMapContract.View, ArcMapPresenter> implements ArcMapContract.View {

    private FragmentArcSceneBinding mBinding;
    private WebTiledLayer imgWebTiledLayer;
    private WebTiledLayer ciaWebTiledLayer;
    private ArcGISScene arcGISScene;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_arc_scene;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        initSceneView();

    }

    private void initSceneView() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud7416273699,none,4N5X0H4AH7AH6XCFK036");
        mBinding.sceneView.setAttributionTextVisible(false);

        {
            imgWebTiledLayer = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_IMAGE_MERCATOR);
            imgWebTiledLayer.loadAsync();
            RequestConfiguration requestConfiguration = new RequestConfiguration();
            requestConfiguration.getHeaders().put("referer", "http://map.tianditu.gov.cn/");
            imgWebTiledLayer.setRequestConfiguration(requestConfiguration);
        }
        {
            ciaWebTiledLayer = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR);
            ciaWebTiledLayer.loadAsync();
            RequestConfiguration requestConfiguration = new RequestConfiguration();
            requestConfiguration.getHeaders().put("referer", "http://map.tianditu.gov.cn/");
            ciaWebTiledLayer.setRequestConfiguration(requestConfiguration);
        }
        arcGISScene = new ArcGISScene();
        arcGISScene.getBasemap().getBaseLayers().add(imgWebTiledLayer);
        arcGISScene.getBasemap().getBaseLayers().add(ciaWebTiledLayer);
        mBinding.sceneView.setScene(arcGISScene);

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.sceneView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.sceneView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.sceneView.dispose();
    }

}
