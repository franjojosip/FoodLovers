package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.account

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.NotificationsManager
import ht.ferit.fjjukic.foodlovers.data.repository.CodeRepository
import ht.ferit.fjjukic.foodlovers.databinding.FragmentAccountBinding
import ht.ferit.fjjukic.foodlovers.ui.base.AccountViewModelFactory
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.AccountViewModel
import ht.ferit.fjjukic.foodlovers.utils.checkNetworkState
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AccountFragment : Fragment(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory by instance<AccountViewModelFactory>()

    private lateinit var notificationManager: NotificationsManager
    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var btnChangeLocation: Button
    private lateinit var btnChangeImage: Button
    private lateinit var btnTakeImage: Button
    private lateinit var ivUserImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(AccountViewModel::class.java)
        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        btnChangeImage = view.findViewById(R.id.btn_change_image)
        btnTakeImage = view.findViewById(R.id.btn_take_image)
        btnChangeLocation = view.findViewById(R.id.btn_change_location)
        ivUserImage = view.findViewById(R.id.iv_account_image)

        viewModel.getUser(view).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loadImage(it.imageUrl)
            }
        })
        setNotificationManager()
        setButtonListener()
    }

    private fun setNotificationManager() {
        notificationManager =
            NotificationsManager(requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    private fun setButtonListener() {
        val navController = requireView().findNavController()
        btnChangeImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    CodeRepository.PERMISSION_CODE
                )
            } else {
                chooseImageFromGallery()
            }
        }
        btnTakeImage.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CodeRepository.PERMISSION_CODE
                )
            } else {
                takeImage()
            }
        }
        btnChangeLocation.setOnClickListener {
            if (!requireContext().checkNetworkState()) {
                Toast.makeText(
                    context,
                    "Turn on internet before using this feature",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                navController.navigate(R.id.nav_location, Bundle().apply {
                    putString("userId", viewModel.currentUser.value!!.userId)
                }, null)
            }
        }
    }

    override fun onSuccess() {
        Toast.makeText(context, "Successfully updated user info!", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun takeImage() {
        val values = ContentValues()
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "")
        val uri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(cameraIntent, CodeRepository.IMAGE_CAPTURE_CODE)
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, CodeRepository.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        if (resultCode == Activity.RESULT_OK && requestCode == CodeRepository.IMAGE_PICK_CODE) {
            viewModel.setImagePath(uri.toString())
            loadImage(uri.toString())
        } else if (resultCode == Activity.RESULT_OK && requestCode == CodeRepository.IMAGE_CAPTURE_CODE) {
            viewModel.setImagePath(uri.toString())
            loadImage(uri.toString())
            notificationManager.sendNotification(
                requireContext(),
                requireActivity(),
                uri!!
            )
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this).load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.app_icon)
            .error(android.R.drawable.stat_notify_error)
            .into(ivUserImage)
    }
}
