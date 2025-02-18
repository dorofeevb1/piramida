package su.panfilov.piramida;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class OurAppsFragment extends Fragment {

    public OurAppsFragment() {
        // Required empty public constructor
    }

    public static OurAppsFragment newInstance() {
        return new OurAppsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_our_apps, container, false);

        // Initialize the back button
        Button backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Initialize the banners and set click listeners
        ImageView bannerNeiroCafe = rootView.findViewById(R.id.bannerNeiroCafe);
        ImageView bannerMetaModern = rootView.findViewById(R.id.bannerMetaModern);
        ImageView bannerBogoban = rootView.findViewById(R.id.bannerBogoban);

        bannerNeiroCafe.setOnClickListener(v -> openAppOrMarket("su.panfilov.neirocafe",
                "https://play.google.com/store/search?q=%D0%9D%D0%B5%D0%B9%D1%80%D0%BE%D0%BA%D0%B0%D1%84%D0%B5&c=apps",
                "https://apps.apple.com/ru/app/%D0%BD%D0%B5%D0%B9%D1%80%D0%BE%D0%BA%D0%B0%D1%84%D0%B5/id1612475980"));

        bannerMetaModern.setOnClickListener(v -> openAppOrMarket("su.panfilov.metamodern",
                "https://play.google.com/store/apps/details?id=su.panfilov.metamodern",
                "https://apps.apple.com/ru/app/metamodern/id1484083509"));

        bannerBogoban.setOnClickListener(v -> openAppOrMarket("su.panfilov.bogoban",
                "https://play.google.com/store/apps/details?id=su.panfilov.bogoban",
                "https://apps.apple.com/ru/app/%D0%B1%D0%BE%D0%B3%D0%BE%D0%B1%D0%B0%D0%BD/id1513833719"));

        return rootView;
    }

    private void openAppOrMarket(String packageName, String playStoreUrl, String appStoreUrl) {
        Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // App is installed, open it
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            requireContext().startActivity(intent);
        } else {
            // App is not installed, open the market link
            String url = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                    ? playStoreUrl
                    : appStoreUrl;
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            requireContext().startActivity(marketIntent);
        }
    }
}
