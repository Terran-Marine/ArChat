package com.gj.archat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.DpToMetersViewSizer
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )// Set up the full screen
        setContentView(R.layout.activity_main)

        initView()
        initArRenderable()
        initArScene()
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var frameLayout: FrameLayout
    private lateinit var chatAdapter: ChatAdapter

    private val msgList = arrayListOf<MsgBean>()

    private fun sendMsg(msg: String?) {
        if (msg.isNullOrEmpty()) {
            ToastUtils.showLong("消息不能為空")
        } else {
            msgList.add(0, MsgBean(msg, 1))
            msgList.add(0, MsgBean("Reply: $msg", 0))
            chatAdapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(msgList.size - 1)
        }
        UI_EditText.setText("")
    }

    private fun initView() {
        UI_EditLayout.visibility = View.GONE
        UI_Send.setOnClickListener {
            sendMsg(UI_EditText.text.toString())
        }

        UI_EditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMsg(UI_EditText.text.toString())
                true
            } else {
                false
            }
        }

        /*------------初始化recyclerView------------*/
        frameLayout = (this@MainActivity.layoutInflater.inflate(R.layout.widget_recycler, null) as FrameLayout)
        chatAdapter = ChatAdapter(this@MainActivity, msgList)
            .apply {
                setEmptyView(R.layout.empty, frameLayout)
            }

        recyclerView = (frameLayout.getChildAt(0) as RecyclerView)
            .apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, true)
                adapter = chatAdapter

            }
    }

    lateinit var viewRenderable: ViewRenderable

    private fun initArRenderable() {
        ViewRenderable.builder()
            .setView(this@MainActivity, frameLayout)
            //A sizer for controlling the size of a ViewRenderable by defining how many dp there are per meter.
            //By default, 250dp equals to 1 meters,
            .setSizer(DpToMetersViewSizer(300))
            .build()
            .thenAccept { recyclerViewRenderable ->
                recyclerViewRenderable.isShadowCaster =
                        false  //Returns true if configured to cast shadows on other renderables.
                viewRenderable = recyclerViewRenderable
            }
            .exceptionally {
                LogUtils.e("初始化失败!")
                return@exceptionally null
            }
    }

    private fun initArScene() {
        (UI_ArSceneView as ArFragmentCN).setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            viewRenderable?.let { renderable ->
                //createAnchor
                val anchorNode = AnchorNode(hitResult.createAnchor())
                anchorNode.setParent((UI_ArSceneView as ArFragment).arSceneView.scene)

                //Create a camera - oriented node on the render object placed on the node
                val faceToCameraNode = FaceToCameraNode()
                faceToCameraNode.setParent(anchorNode)

                faceToCameraNode.localPosition = Vector3(0.0f, 0.3f, 0.5f)  //Keep node away from the ground

                faceToCameraNode.renderable = renderable

                (UI_ArSceneView as ArFragmentCN).arSceneView.planeRenderer.isEnabled = false
                this@MainActivity.runOnUiThread {
                    UI_EditLayout.visibility = View.VISIBLE
                }
            }
        }
    }
}
