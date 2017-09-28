Support Android minSdk 21

please use StickyHeadersScrollView.markFix(View v) or StickyHeadersNestedScrollView.markFix(View v);
Parameter(View v) should be  ScrollView's onlyChild's direct child;
so View v will to be stickyHeader. 


