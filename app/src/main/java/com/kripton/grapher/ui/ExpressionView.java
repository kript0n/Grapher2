package com.kripton.grapher.ui;

import com.kripton.grapher.R;
import com.kripton.grapher.imp.Graph;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class ExpressionView extends EditText implements TextWatcher {

	private GraphView graphView;
	
	private Graph graph = new Graph();
	
	@SuppressWarnings("unused")
	private Pool<Point> pool;
	
	
	public ExpressionView(Context context) {
		super(context);
		init();
	}
	
	public ExpressionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ExpressionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
	public void setGraphView(GraphView graphView) {
		this.graphView = graphView;
		graph.setGraphViewInfo(graphView.getWidth(), graphView.getHeight(), graphView.getScale());
	}
	
	
	public void setPointsPool(Pool<Point> pool) {
		graph.setPointPool(pool);
	}
	
	
	private void init() {
		this.setTextColor(Color.argb(255, 0, 0, 0));
		this.setInputType(InputType.TYPE_CLASS_TEXT);
		this.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_FULLSCREEN);
		this.setAllCaps(false);
		this.setBackground(getResources().getDrawable(R.drawable.expression_view));
		this.addTextChangedListener(this);
		this.setCursorVisible(true);
		this.setClickable(true);
		this.setFocusable(true);
	}
	
	
	
	@Override
	public void afterTextChanged(Editable arg0) {
		
		/*Delete previous graph*/
		graphView.deleteGraph(graph);
		
		/*if expression is empty*/
		if(arg0.toString().isEmpty()) {
			return;
		}
		
		/*Set new expression then calculating points and add graph to graphs view*/
		graph.setExpression(arg0.toString());
		//graph.calculatePoints();
		graphView.addGraph(graph);
		
	}	
	
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	
	@Override 
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

}
