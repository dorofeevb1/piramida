import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<String>> activeItems = new MutableLiveData<>();

    public void setActiveItems(List<String> items) {
        activeItems.setValue(items);
    }

    public MutableLiveData<List<String>> getActiveItems() {
        return activeItems;
    }
}
