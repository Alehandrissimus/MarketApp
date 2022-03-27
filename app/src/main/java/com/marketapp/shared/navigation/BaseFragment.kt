package com.marketapp.shared.navigation

import androidx.fragment.app.Fragment
import com.marketapp.NavigationActivity

abstract class BaseFragment : Fragment() {

    protected val navigator: Navigator by lazy { (requireActivity() as NavigationActivity).navigator }

}