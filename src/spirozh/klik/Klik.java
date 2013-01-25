package spirozh.klik;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Klik extends Activity {

	private int _level;
	private int _points;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_klik);

		View parent = (View) findViewById(R.id.levelProgress).getParent();
		parent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clicked();
			}
		});

		parent.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				reset();
				return true;
			}
		});
	}

	protected void onStart() {
		super.onStart();

		// load _points
		_points = (int) getPreferences(Context.MODE_PRIVATE).getLong(
				getString(R.string.points), 0);

		_level = levelForPoints(_points);

		// prepare progress bar
		((ProgressBar) findViewById(R.id.levelProgress))
				.setMax(pointsForLevel(_level + 1) - pointsForLevel(_level));

		updateLabels();
	}

	protected void onStop() {
		super.onStop();

		// save _level and _xp
		getPreferences(Context.MODE_PRIVATE).edit()
				.putLong(getString(R.string.points), _points).commit();
	}

	private void reset() {
		_points = _level = 0;

		updateProgressMax();
		updateLabels();
	}

	public void clicked() {
		if (_points++ >= pointsForLevel(_level + 1)) {
			_level++;
			updateProgressMax();
		}

		updateLabels();
	}

	static int pointsForLevel(int level) {
		return fib(level + 6) - fib(6);
	}

	static int levelForPoints(int points) {
		int l = 0;
		while (points > pointsForLevel(l + 1))
			l++;
		return l;
	}

	static int fib(int x) {
		int a = 0;
		int b = 1;

		while (x-- > 0) {
			int t = b;
			b = a + b;
			a = t;
		}

		return a;
	}

	private void updateProgressMax() {
		((ProgressBar) findViewById(R.id.levelProgress))
				.setMax(pointsForLevel(_level + 1) - pointsForLevel(_level));
	}

	private void updateLabels() {
		((TextView) findViewById(R.id.pointsLabel))
				.setText(getString(R.string.points_prefix) + _points);
		((TextView) findViewById(R.id.levelLabel))
				.setText(getString(R.string.level_prefix) + _level);

		((ProgressBar) findViewById(R.id.levelProgress)).setProgress(_points
				- pointsForLevel(_level));
	}

}
