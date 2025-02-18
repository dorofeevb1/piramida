package su.panfilov.piramida;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OurAppsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_our_apps, container, false);

        // Находим кнопку "Назад" и устанавливаем обработчик нажатия
        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Находим баннеры и устанавливаем обработчики нажатия
        ImageView neuroCafeAndroid = view.findViewById(R.id.neuroCafeAndroid);
        ImageView neuroCafeIOS = view.findViewById(R.id.neuroCafeIOS);
        ImageView metaModernAndroid = view.findViewById(R.id.metaModernAndroid);
        ImageView metaModernIOS = view.findViewById(R.id.metaModernIOS);
        ImageView boGobanAndroid = view.findViewById(R.id.boGobanAndroid);
        ImageView boGobanIOS = view.findViewById(R.id.boGobanIOS);

        neuroCafeAndroid.setOnClickListener(v -> openAppOrStore("su.panfilov.neurocafe", "https://play.google.com/store/search?q=%D0%9D%D0%B5%D0%B9%D1%80%D0%BE%D0%BA%D0%B0%D1%84%D0%B5&c=apps"));
        neuroCafeIOS.setOnClickListener(v -> openAppOrStore(null, "https://apps.apple.com/ru/app/%D0%BD%D0%B5%D0%B9%D1%80%D0%BE%D0%BA%D0%B0%D1%84%D0%B5/id1612475980"));
        metaModernAndroid.setOnClickListener(v -> openAppOrStore("su.panfilov.metamodern", "https://play.google.com/store/apps/details?id=su.panfilov.metamodern"));
        metaModernIOS.setOnClickListener(v -> openAppOrStore(null, "https://apps.apple.com/ru/app/metamodern/id1484083509"));
        boGobanAndroid.setOnClickListener(v -> openAppOrStore("su.panfilov.bogoban", "https://play.google.com/store/apps/details?id=su.panfilov.bogoban"));
        boGobanIOS.setOnClickListener(v -> openAppOrStore(null, "https://apps.apple.com/ru/app/%D0%B1%D0%BE%D0%B3%D0%BE%D0%B1%D0%B0%D0%BD/id1513833719"));

        return view;
    }

    private void openAppOrStore(String packageName, String storeUrl) {
        if (packageName != null) {
            try {
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    openStore(storeUrl);
                }
            } catch (Exception e) {
                openStore(storeUrl);
            }
        } else {
            openStore(storeUrl);
        }
    }

    private void openStore(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}