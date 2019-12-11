package com.swpj.pre1012

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_result10.*


class ResultFrag : Fragment() { //모든 fragment가 정의된 클래스들을 모아놓음
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button: Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener { //위에 정의된 fragment 내 X버튼을 누르면 해당 fragment를 종료시키는 리스너
            if (activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag2 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag3 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag4 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag5 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag6 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag7 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag8 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag9 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag10 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}


class ResultFrag11 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag12 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag13 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag14 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag15 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag16 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag17 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag18 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag19 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag20 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag21 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag22 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}

class ResultFrag23 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}


class ResultFragX : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_result10, container, false)
        val button : Button = v.findViewById(R.id.buttonX)

        button.setOnClickListener {
            if(activity != null) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
        return v
    }
}