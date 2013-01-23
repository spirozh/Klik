package spirozh.klik;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Klik extends Activity {

	private int _level;
	private int _xp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_klik);

        View.OnLongClickListener lcl = new View.OnLongClickListener() {
        	@Override
        	public boolean onLongClick(View view) {
        		reset();
        		return true;
        	}
        };
        
        View.OnClickListener cl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clicked();
			}
		};

		View parent = (View) findViewById(R.id.levelProgress).getParent();
        parent.setOnLongClickListener(lcl);
        parent.setOnClickListener(cl);
    }
    
    protected void onStart() {
    	super.onStart();
    	
        // load _level and _xp
    	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    	_xp = (int) sharedPref.getLong(getString(R.string.xp), 0);
    	_level = (int) sharedPref.getLong(getString(R.string.level), 0);

    	// prepare progress bar
		((ProgressBar) findViewById(R.id.levelProgress)).setMax(xpForLevel(_level+1) - xpForLevel(_level));
		
		updateLabels();
    }
    
    protected void onStop() {
    	super.onStop();
    	
    	// save _level and _xp
    	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putLong(getString(R.string.xp), _xp);
    	editor.putLong(getString(R.string.level), _level);
    	editor.commit();
    }
    
    private void reset() {
    	_xp = 0;
    	_level = 0;
    	
    	ProgressBar pb = (ProgressBar) findViewById(R.id.levelProgress);
    	pb.setMax(xpForLevel(1));

    	updateLabels();
    }
    
    public void clicked() {
    	_xp++;
    	
    	if (_xp >= xpForLevel(_level + 1)) {
    		_level++;
    		((ProgressBar) findViewById(R.id.levelProgress)).setMax(xpForLevel(_level+1) - xpForLevel(_level));
    	}
    	
    	updateLabels();
    }
    
    static int xpForLevel(int level) {
    	return fib(level + 6) - fib(6);
    }
    
    static int fib(int x) {
    	int a = 0;
    	int b = 1;
    	
    	while (x > 0) {
    		int t = b;
    		b = a+b;
    		a = t;
    		x--;
    	}
    	
    	return a;
    }

	private void updateLabels() {
		((TextView) findViewById(R.id.experienceLabel)).setText(getString(R.string.xp_label_prefix) + + _xp);
		((TextView) findViewById(R.id.levelLabel)).setText(getString(R.string.level_label_prefix) + _level);
		
		((ProgressBar) findViewById(R.id.levelProgress)).setProgress(_xp - xpForLevel(_level));
	}

    
}
