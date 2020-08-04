package ht.ferit.fjjukic.foodlovers.ui.main.view.ui.account

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ht.ferit.fjjukic.foodlovers.R
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
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var btnChangeLocation: Button
    private lateinit var btnChangeImage: Button
    private lateinit var btnTakeImage: Button
    private val imageCaptureCode: Int = 10
    private var imageUri: Uri? = null

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

    override fun onSuccess() {
        Toast.makeText(context, "Successfully updated user info!", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        btnChangeImage = view.findViewById(R.id.btn_change_image)
        btnTakeImage = view.findViewById(R.id.btn_take_image)
        btnChangeLocation = view.findViewById(R.id.btn_change_location)
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            viewModel.currentUser.value = it
            if(it != null){
                viewModel.setImagePath(it.imagePath)
                viewModel.setLocation(view)
            }
        })
        setNotificationManager()
        setButtonListener()
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
            if(!requireContext().checkNetworkState()){
                Toast.makeText(context, "Turn on internet before using this feature", Toast.LENGTH_SHORT).show()
            }
            else{
                navController.navigate(R.id.nav_location, Bundle().apply {
                    putInt("userId", viewModel.currentUser.value!!.id)
                }, null)
            }
        }
    }

    private fun takeImage() {
        val values = ContentValues()
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "")
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, imageCaptureCode)
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, CodeRepository.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CodeRepository.IMAGE_PICK_CODE) {
            viewModel.setImagePath(data?.data.toString())
        } else if (resultCode == Activity.RESULT_OK && requestCode == imageCaptureCode) {
            viewModel.setImagePath(imageUri.toString())
            sendNotification()
        }
    }


    private fun sendNotification() {
        val intent = Intent(Intent.ACTION_VIEW, imageUri)
        val pendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(requireActivity().packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title, "Spremljena je nova slika")
        contentView.setImageViewUri(R.id.iv_picture, imageUri)

        notificationChannel = NotificationChannel(
            R.string.channelId.toString(),
            R.string.notificationTitle.toString(),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        val builder = NotificationCompat.Builder(requireContext(), R.string.channelId.toString())
            .setContent(contentView)
            .setSmallIcon(R.drawable.app_icon)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.app_icon))
            .setContentIntent(pendingIntent)

        notificationManager.notify(1234, builder.build())
    }

    private fun setNotificationManager() {
        notificationManager =  requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}
