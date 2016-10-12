package ired.kotlindemo

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isRevertAnimation = false;
        val animationViewGroup: AnimationViewGroup = findViewById(R.id.view_group) as AnimationViewGroup;
        val button: Button = findViewById(R.id.button) as Button;

        val animationViewGroup2: AnimationViewGroup2 = findViewById(R.id.view_group2) as AnimationViewGroup2;
        val button2: Button = findViewById(R.id.button2) as Button;

        val settingMenuBtn = findViewById(R.id.setting_menu);

        button.setOnClickListener {
            if (!isRevertAnimation) {
                animationViewGroup.startAnimation();
                isRevertAnimation = true;
                button.setText("REVERT ANIMATION")
            } else {
                animationViewGroup.reverseAnimation();
                isRevertAnimation = false;
                button.setText("ANIMATION")
            }
        }

        button2.setOnClickListener {

            if (!isRevertAnimation) {
                animationViewGroup2.startAnimation();
                isRevertAnimation = true;
                button2.setText("REVERT ANIMATION")
            } else {
                animationViewGroup2.reverseAnimation();
                isRevertAnimation = false;
                button2.setText("ANIMATION")
            }
        }

        settingMenuBtn.setOnClickListener {
            var currentItems = mutableListOf<FloatingActionButton>();

            for (x in 0..5) {
                var button: FloatingActionButton = FloatingActionButton(this);
                button.z = 0.0f;
                button.compatElevation = 0.0f;
                button.setImageResource(R.drawable.square_72px);
                currentItems.add(button);
            }

            animationViewGroup2.setMenuItems(currentItems);
        }
    }
}
