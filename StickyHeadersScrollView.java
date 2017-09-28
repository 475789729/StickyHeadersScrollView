package com.example.dell.testapp;

        import android.content.Context;
        import android.util.AttributeSet;
        import android.view.View;
        import android.widget.ScrollView;
        import java.util.ArrayList;
        import java.util.List;

/**
 *   use  function markFix(View v);
 *   View v should be  ScrollView's onlyChild's direct child;
 */
public class StickyHeadersScrollView extends ScrollView {
    private OnScrollChangeListener mOnScrollChangeListener;

    private List<View> fixViews = new ArrayList<View>();
    private List<Integer> getTops = new ArrayList<Integer>();
    private  View movedView;


    public StickyHeadersScrollView(Context context) {
        this(context, null);
    }

    public StickyHeadersScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyHeadersScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public interface OnScrollChangeListener{
        void onScrollChanged(ScrollView scrollView,int l, int t, int oldl, int oldt);
    }

    public void setOnScrollChangeListen(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangeListener!=null){
            mOnScrollChangeListener.onScrollChanged(this,l,t,oldl,oldt);

        }
    }
     //View v should be  ScrollView's onlyChild's direct child;
     public void markFix(final View v){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(fixViews.size() == 0){
                        fixViews.add(v);
                        getTops.add(v.getTop());
                    }else{
                        boolean b = false;
                        for(int i = fixViews.size() - 1; i >=0; i--){
                            View compareView = fixViews.get(i);
                            if(v.getTop() >= compareView.getTop()){
                                fixViews.add(i + 1, v);
                                getTops.add(i + 1, v.getTop());
                                b = true;
                                break;
                            }
                        }
                        if(!b){
                            fixViews.add(0, v);
                            getTops.add(0, v.getTop());
                        }
                    }
                    setOnScrollChangeListen(new OnScrollChangeListener() {
                        @Override
                        public void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt) {
                            work(scrollView.getScrollY());
                        }
                    });
                }
            },10);

     }

     public void clearFix(){
         fixViews.clear();
         getTops.clear();
         setOnScrollChangeListen(null);
     }

     private void work(int scrollY){
           View shouldFixView = null;
           for(int i = fixViews.size() - 1; i >= 0; i--){
               View index = fixViews.get(i);
               if(isViewHideInTop(index, scrollY)){
                   shouldFixView = index;
                   break;
               }else{
                   if(i - 1 >= 0){
                       if(index.getTop() - scrollY < fixViews.get(i - 1).getMeasuredHeight()){
                           shouldFixView = null;
                           break;
                       }
                   }
               }
           }
           if(shouldFixView != null){
               transform(shouldFixView, scrollY);
           }else{
               restoreView();
           }
     }

     private boolean isViewHideInTop(View v, int scrollY){
         return scrollY > v.getBottom();
     }

      private void transform(View v, int scrollY){
          restoreView();
          movedView = v;
          v.setTranslationY(scrollY - v.getTop());
          v.setTranslationZ(100f);
      }

      private void restoreView(){
          if(movedView != null){
              movedView.setTranslationY(0f);
              movedView.setTranslationZ(0f);
          }
      }
}
